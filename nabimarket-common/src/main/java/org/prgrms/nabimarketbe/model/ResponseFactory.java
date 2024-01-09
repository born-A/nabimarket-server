package org.prgrms.nabimarketbe.model;

public class ResponseFactory {
    // 단일건 결과 처리 메소드
    public static <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);

        return result;
    }

    // 성공 결과만 처리
    public static CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);

        return result;
    }

    // 실패 결과만 처리
    public static CommonResult getFailResult(String code, String msg) {
        CommonResult result = new CommonResult();
        setFailResult(
            result,
            code,
            msg
        );

        return result;
    }

    // API 요청 성공 시 응답 모델을 성공 데이터로 세팅
    private static void setSuccessResult(CommonResult result) {
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMsg());
    }

    // API 요청 실패 시 응답 모델을 실패 데이터로 세팅
    private static void setFailResult(CommonResult result, String code, String msg) {
        result.setCode(code);
        result.setMessage(msg);
    }
}
