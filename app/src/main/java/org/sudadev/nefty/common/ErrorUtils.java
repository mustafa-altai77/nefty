package org.sudadev.nefty.common;

import android.content.Context;
import android.widget.Toast;

import org.sudadev.nefty.models.ErrorResponse;
import org.sudadev.nefty.networks.ServiceGenerator;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {
    public static ErrorResponse showError(Response response, Context context) {
        ErrorResponse errorMessage = null;

        try {
            errorMessage = ErrorUtils.parseError(response);

            Toast.makeText(context,
                    errorMessage.getMessage() + "\n" +
                            errorMessage.getErrors().get(0) != null ?
                            errorMessage.getErrors().get(0) : " ",
                    Toast.LENGTH_LONG).show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return errorMessage;
    }

    private static ErrorResponse parseError(Response<?> response) {
        ErrorResponse error;

        try {
            Converter<ResponseBody, ErrorResponse> converter =
                    ServiceGenerator.retrofit()
                            .responseBodyConverter(ErrorResponse.class, new Annotation[0]);

            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
            return new ErrorResponse();
        }

        return error;
    }
}
