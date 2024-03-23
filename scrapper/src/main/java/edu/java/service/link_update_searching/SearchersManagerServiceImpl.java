package edu.java.service.link_update_searching;

import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.service.link_update_searching.searchers.LinkUpdateSearcher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchersManagerServiceImpl implements SearchersManagerService {
    private final List<LinkUpdateSearcher> linkUpdateSearcherList;

    @Override
    public List<LinkUpdate> getUpdates(LinkDto linkDto) {
        for (LinkUpdateSearcher linkUpdateSearcher : linkUpdateSearcherList) {
            if (linkUpdateSearcher.canProcess(linkDto.url())) {
                return linkUpdateSearcher.getUpdates(linkDto);
            }
        }
        return List.of();
    }
}
