package org.idea.smokecast.fireEvent.dto;

import java.util.List;

public record FireEventBulkPayload(List<FireEventBulkRequest> items) {
}
