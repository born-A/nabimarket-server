package org.prgrms.nabimarketbe.jpa.dibs.projection;

import java.util.List;

public record DibListReadPagingResponseDTO(
    List<DibListReadResponseDTO> dibList,
    Long nextCursorId
) {
    public static DibListReadPagingResponseDTO of(
        List<DibListReadResponseDTO> dibList,
        Long nextCursorId
    ) {
        return new DibListReadPagingResponseDTO(dibList, nextCursorId);
    }
}
