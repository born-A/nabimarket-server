package org.prgrms.nabimarketbe.dibs.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.dibs.projection.DibListReadResponseDTO;

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
