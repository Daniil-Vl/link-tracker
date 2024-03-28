package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.StackoverflowQuestionAnswersResponse;
import edu.java.dto.stackoverflow.StackoverflowQuestionResponse;

public interface StackoverflowClient {
    /**
     * Return stackoverflow question using id
     *
     * @param questionId - id of question
     * @return record containing question id, question title, and last activity date
     */
    StackoverflowQuestionResponse getQuestion(Long questionId);

    StackoverflowQuestionAnswersResponse getAnswers(Long questionId);
}
