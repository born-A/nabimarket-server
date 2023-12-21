package org.prgrms.nabimarketbe.dibs.wrapper;

public record DibResponseDTO<T>(
    T dibInfo
) {
}
