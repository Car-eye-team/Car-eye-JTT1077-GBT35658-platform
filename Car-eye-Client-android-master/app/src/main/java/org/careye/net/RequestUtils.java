package org.careye.net;

class RequestUtils {private static final String TAG = RequestUtils.class.getName();

    /**
     * Methods: validate
     * Description: 判断网络请求码成功与失败
     * @exception null
     * @param requestCode
     * @return
     * String 空格为成功  有字符为失败内容为失败原因
     */

    public static String validate(String requestCode) {

      /*  if (!StringUtils.isEmpty(requestCode)&&!"0".equals(requestCode)) {
            TypedArray resultCodes = DemoApplication.getResource()
                    .obtainTypedArray(R.array.network_result_code_select);
            TypedArray resultexplains = DemoApplication.getResource()
                    .obtainTypedArray(R.array.network_result_explain_select);

            for (int i = 0; i < resultCodes.length(); i++) {
                if (requestCode.equals(resultCodes.getString(i))) {
                    if (resultexplains.length() > i) {
                        return resultexplains.getString(i);
                    }
                }
            }
            return "未知错误:"+requestCode;
        }*/
        return "";
    }


}
