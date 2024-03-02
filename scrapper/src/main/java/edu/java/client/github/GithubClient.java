package edu.java.client.github;

import edu.java.dto.github.GithubEventResponse;
import java.util.List;

public interface GithubClient {
    /**
     * Return list of last events in the GitHub repository
     *
     * @param author          - author of repository
     * @param repository      - repository, where search new events
     * @param numberOfUpdates - number of recent updates in the repository, which will be returned
     * @return list of n = numberOfUpdates recent updates
     */
    List<GithubEventResponse> getLatestUpdates(String author, String repository, int numberOfUpdates);
}
