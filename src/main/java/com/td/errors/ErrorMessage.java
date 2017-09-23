package com.td.errors;

import com.fasterxml.jackson.annotation.JsonView;
import com.td.errors.views.ErrorViews;

import java.util.List;

public class ErrorMessage {
    @JsonView(ErrorViews.Default.class)
    private ErrorTypes type;

    @JsonView(ErrorViews.AuthorizationError.class)
    private AuthorizationError authorizationError;

    @JsonView(ErrorViews.IncorrectRequestData.class)
    private List<IncorrectRequestDataError> incorrectRequestDataErrors;

    public static ErrorMessageBuilder builder() {
        return new ErrorMessage().new ErrorMessageBuilder();
    }

    public final class ErrorMessageBuilder {

        private ErrorMessageBuilder() {

        }

        public ErrorMessageBuilder setType(ErrorTypes inType) {
            ErrorMessage.this.type = inType;
            return this;
        }

        public ErrorMessageBuilder setAuthorizationErrors(AuthorizationError error) {
            ErrorMessage.this.authorizationError = error;
            return this;
        }

        public ErrorMessageBuilder setIncorrectRequestDataErrors(List<IncorrectRequestDataError> errors) {
            ErrorMessage.this.incorrectRequestDataErrors = errors;
            return this;
        }

        public ErrorMessage build() {
            return ErrorMessage.this;
        }
    }

}
