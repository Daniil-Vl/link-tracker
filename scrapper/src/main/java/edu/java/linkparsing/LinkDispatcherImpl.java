package edu.java.linkparsing;

import edu.java.dto.LinkUpdate;
import edu.java.dto.dao.LinkDto;
import edu.java.resource.ResourceManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkDispatcherImpl implements LinkDispatcher {
    private final List<ResourceManager> resourceManagerList;

    @Override
    public List<LinkUpdate> getUpdates(LinkDto linkDto) {
        for (ResourceManager resourceManager : resourceManagerList) {
            if (resourceManager.canProcess(linkDto.url())) {
                return resourceManager.getUpdates(linkDto);
            }
        }
        return List.of();
    }
}
