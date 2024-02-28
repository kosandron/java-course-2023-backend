package edu.java.client.stackoverflow;

import edu.java.client.dto.StackOverFlowResponse;
import org.jetbrains.annotations.NotNull;

public interface StackOverflowClient {
    StackOverFlowResponse fetchQuestion(@NotNull Long questionId);
}
