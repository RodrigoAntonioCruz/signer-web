package com.signer.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -5241554286656150984L;

	@JsonIgnore
	private HttpStatus httpStatusCode;
	private String timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;

	public BusinessException() {
	}

	public BusinessException(HttpStatus httpStatusCode, String timestamp, Integer status, String error, String message, String path) {
		this.httpStatusCode = httpStatusCode;
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public HttpStatus getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(HttpStatus httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public BusinessExceptionBody getOnlyBody() {
		return BusinessExceptionBody.builder()
				.timestamp(this.timestamp)
				.status(this.status)
				.error(this.error)
				.message(this.message)
				.path(this.path)
				.build();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BusinessException that = (BusinessException) o;
		return Objects.equals(httpStatusCode, that.httpStatusCode) &&
				Objects.equals(timestamp, that.timestamp) &&
				Objects.equals(status, that.status) &&
				Objects.equals(error, that.error) &&
				Objects.equals(message, that.message) &&
				Objects.equals(path, that.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(httpStatusCode, timestamp, status, error, message, path);
	}

	// Implementação do Builder
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private HttpStatus httpStatusCode;
		private String timestamp;
		private Integer status;
		private String error;
		private String message;
		private String path;

		public Builder httpStatusCode(HttpStatus httpStatusCode) {
			this.httpStatusCode = httpStatusCode;
			return this;
		}

		public Builder timestamp(String timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder status(Integer status) {
			this.status = status;
			return this;
		}

		public Builder error(String error) {
			this.error = error;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public BusinessException build() {
			return new BusinessException(httpStatusCode, timestamp, status, error, message, path);
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class BusinessExceptionBody {

		private String timestamp;
		private Integer status;
		private String error;
		private String message;
		private String path;

		public BusinessExceptionBody() {
		}

		public BusinessExceptionBody(String timestamp, Integer status, String error, String message, String path) {
			this.timestamp = timestamp;
			this.status = status;
			this.error = error;
			this.message = message;
			this.path = path;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			BusinessExceptionBody that = (BusinessExceptionBody) o;
			return Objects.equals(timestamp, that.timestamp) &&
					Objects.equals(status, that.status) &&
					Objects.equals(error, that.error) &&
					Objects.equals(message, that.message) &&
					Objects.equals(path, that.path);
		}

		@Override
		public int hashCode() {
			return Objects.hash(timestamp, status, error, message, path);
		}

		// Implementação do Builder para BusinessExceptionBody
		public static Builder builder() {
			return new Builder();
		}

		public static class Builder {
			private String timestamp;
			private Integer status;
			private String error;
			private String message;
			private String path;

			public Builder timestamp(String timestamp) {
				this.timestamp = timestamp;
				return this;
			}

			public Builder status(Integer status) {
				this.status = status;
				return this;
			}

			public Builder error(String error) {
				this.error = error;
				return this;
			}

			public Builder message(String message) {
				this.message = message;
				return this;
			}

			public Builder path(String path) {
				this.path = path;
				return this;
			}

			public BusinessExceptionBody build() {
				return new BusinessExceptionBody(timestamp, status, error, message, path);
			}
		}
	}
}
