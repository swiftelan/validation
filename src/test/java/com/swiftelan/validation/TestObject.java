package com.swiftelan.validation;

import javax.validation.constraints.NotNull;

public class TestObject {

	@NotNull
	String notNullAnnotation;

	String notNullRuntime;

	String allowNull;
}
