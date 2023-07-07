package com.inovusbot.mytestbot.module.jira.service;

import com.inovusbot.mytestbot.module.jira.model.Issue;
import com.inovusbot.mytestbot.module.jira.model.IssueWorklog;
import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JiraService {

    private final MessageSenderService messageSenderService;
    private final UserService userService;


    private final WorklogsService worklogsService;

    public JiraService(MessageSenderService messageSenderService, UserService userService, WorklogsService worklogsService) {
        this.messageSenderService = messageSenderService;
        this.userService = userService;
        this.worklogsService = worklogsService;
    }

    public void showAllTasks(String userId) {
        List<Issue> issues = getInProgressTaskList();
        StringBuilder issuesAndSummary = new StringBuilder();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        issues.forEach(issue -> {
            issuesAndSummary.append(issue.getLabel()).append(" ").append(issue.getSummary()).append("\n");

            InlineKeyboardButton worklogsButton = new InlineKeyboardButton();
            worklogsButton.setText(issue.getLabel());
            worklogsButton.setCallbackData("jira-worklogs-time-" + issue.getLabel() + "-0-0");
            List<InlineKeyboardButton> worklogsRow = new ArrayList<>();
            worklogsRow.add(worklogsButton);
            rowsInLine.add(worklogsRow);
        });

        String text = "Над эти задачами ты сегодня работал. По крайней мере, должен был: \n\n" +
                issuesAndSummary + "\n" +
                "Выбирай на какую задачу будем списывать твою прокрастинацию.";

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/jira");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);
        rowsInLine.add(backRow);

        inlineKeyboardMarkup.setKeyboard(rowsInLine);

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);

        userService.setContext(userId, "jira-worklogs");
    }

    public void setWorklogTime(String userId, String issue, Integer hours, Integer minutes) {
        System.out.println("Issee: " + issue );
        String text = """
                Установи время для задачи %s:
                
                """.formatted(issue);


        List<InlineKeyboardButton> hoursRow = new ArrayList<>();
        InlineKeyboardButton hoursTitleButton = new InlineKeyboardButton();
        hoursTitleButton.setText("Часы: ");
        hoursTitleButton.setCallbackData("do-nothing");
        hoursRow.add(hoursTitleButton);

        InlineKeyboardButton hoursMinusButton = new InlineKeyboardButton();
        hoursMinusButton.setText("➖");
        hoursMinusButton.setCallbackData("jira-worklogs-time-" + issue + "-" +
                (hours < 1 ? 0 : hours - 1)
                + "-" + minutes);
        hoursRow.add(hoursMinusButton);

        InlineKeyboardButton hoursCountButton = new InlineKeyboardButton();
        hoursCountButton.setText(hours.toString());
        hoursCountButton.setCallbackData("do-nothing");
        hoursRow.add(hoursCountButton);

        InlineKeyboardButton hoursPlusButton = new InlineKeyboardButton();
        hoursPlusButton.setText("➕");
        hoursPlusButton.setCallbackData("jira-worklogs-time-" + issue + "-" +
                (hours > 11 ? 12 : hours + 1)
                + "-" + minutes);
        hoursRow.add(hoursPlusButton);


        List<InlineKeyboardButton> minutesRow = new ArrayList<>();
        InlineKeyboardButton minutesTitleButton = new InlineKeyboardButton();
        minutesTitleButton.setText("Минуты: ");
        minutesTitleButton.setCallbackData("do-nothing");
        minutesRow.add(minutesTitleButton);

        InlineKeyboardButton minutesMinusButton = new InlineKeyboardButton();
        minutesMinusButton.setText("➖");
        minutesMinusButton.setCallbackData("jira-worklogs-time-" + issue + "-" + hours + "-"+
                (minutes < 5 ? 0 : minutes - 5));
        minutesRow.add(minutesMinusButton);

        InlineKeyboardButton minutesCountButton = new InlineKeyboardButton();
        minutesCountButton.setText(minutes.toString());
        minutesCountButton.setCallbackData("do-nothing");
        minutesRow.add(minutesCountButton);

        InlineKeyboardButton minutesPlusButton = new InlineKeyboardButton();
        minutesPlusButton.setText("➕");
        minutesPlusButton.setCallbackData("jira-worklogs-time-" + issue + "-" + hours + "-"+
                (minutes > 55 ? 55 : minutes + 5));
        minutesRow.add(minutesPlusButton);

        InlineKeyboardButton confirmButton = new InlineKeyboardButton();
        confirmButton.setText("Подтвердить");
        if(hours == 0 && minutes == 0) {
            confirmButton.setCallbackData("do-nothing");
        } else {
            confirmButton.setCallbackData("jira-worklogs-confirm-" + issue + "-" + hours + "-" + minutes);
        }

        List<InlineKeyboardButton> confirmRow = new ArrayList<>();
        confirmRow.add(confirmButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("jira-worklogs");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(hoursRow, minutesRow, confirmRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "jira-worklogs-time");
    }



    private List<Issue> getInProgressTaskList() {
        return worklogsService.getInProgress().getIssues().stream()
                .map(issue -> Issue.builder()
                        .label(issue.getKey())
                        .summary(issue.getFields().getSummary())
                        .build())
                .collect(Collectors.toList());
    }

    public void confirmWorklog(String userId, String issue, Integer hours, Integer minutes) {
        IssueWorklog issueWorklog = new IssueWorklog();
        issueWorklog.setIssueId(issue);
        issueWorklog.setMinutes(hours * 60 + minutes);

        worklogsService.addWorklogs(List.of(issueWorklog));


        String hoursString = "";
        String minutesString = "";

        if(hours != 0) {
            if(hours == 1) {
                hoursString += "целый " + hours.toString() + " унылый час";
            } else if (hours > 1 && hours < 5) {
                hoursString += hours.toString() + " бесцельно потраченных часа";
            } else {
                hoursString += hours.toString() + " бесцельно потраченных часов";
            }
        }

        if(minutes != 0) {
            if(hours != 0) {
                minutesString += "и ";
            } else {
                minutesString += "жалких ";
            }
            minutesString += minutes.toString() + " минут";
        }

        String text = """
                Отлично, к задаче %s ты добавил %s %s 🎉
                
                Продолжай в том же духе!
                """.formatted(issue, hoursString, minutesString);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("jira-worklogs");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardButton menuButton = new InlineKeyboardButton();
        menuButton.setText("В главное меню");
        menuButton.setCallbackData("/menu");
        List<InlineKeyboardButton> menuRow = new ArrayList<>();
        menuRow.add(menuButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(backRow, menuRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "jira-main");
    }

    public void statusMenu(String userId) {
        String text = """
                Знаешь, мне не хочется этим заниматься... Может просто удалим все твои задачи?
                
                Прощай, жестокий мир!
                """;

        InlineKeyboardButton deleteButton = new InlineKeyboardButton();
        deleteButton.setText("Удалить все задачи");
        deleteButton.setCallbackData("jira-status-delete-all");
        List<InlineKeyboardButton> deleteRow = new ArrayList<>();
        deleteRow.add(deleteButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/jira");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(deleteRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "jira-status");
    }

    public void statusDone(String userId) {
        String text = """
                Почему-то у меня не получилось это сделать☹️
                
                Ничего страшного, давай попробуем завтра?😉
                """;

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/jira");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "jira-status-done");
    }

    public void showMenu(String userId) {
        String text = """
                Здесь ты можешь управлять задачами в Jira:
                
                """;

        InlineKeyboardButton worklogsButton = new InlineKeyboardButton();
        worklogsButton.setText("⏳ Добавить ворклоги");
        worklogsButton.setCallbackData("jira-worklogs");
        List<InlineKeyboardButton> worklogsRow = new ArrayList<>();
        worklogsRow.add(worklogsButton);

        InlineKeyboardButton editButton = new InlineKeyboardButton();
        editButton.setText("✅ Изменить статус задачи");
        editButton.setCallbackData("jira-status");
        List<InlineKeyboardButton> editRow = new ArrayList<>();
        editRow.add(editButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/menu");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(worklogsRow, editRow , backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "jira-main");
    }
}
