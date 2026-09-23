package org.idea.smokecast.fireDetection.dto;

import java.util.List;

public record FireDetectionBulkPayload(List<FireDetectionBulkRequest> items) {
}
