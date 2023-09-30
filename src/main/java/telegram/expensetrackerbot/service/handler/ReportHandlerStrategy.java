package telegram.expensetrackerbot.service.handler;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.enums.CommandState;

@Component
public class ReportHandlerStrategy {
    private final List<ReportHandler> reportHandlers;

    public ReportHandlerStrategy(List<ReportHandler> reportHandlers) {
        this.reportHandlers = reportHandlers;
    }

    public ReportHandler getHandler(CommandState commandState) {
        return reportHandlers.stream()
                .filter(handler -> handler.isApplicable(commandState))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }
}
