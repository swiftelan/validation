package com.swiftelan.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RuntimeValidationProviderTest {
	private Validator validator;

	@Before
	public void before() {
		ValidatorFactory validatorFactory = Validation.byProvider(RuntimeValidationProvider.class).configure()
				.buildValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	public void notNullAtRuntimeIsNull() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "notNullRuntime", null);
		Assert.assertFalse(violations.isEmpty());
	}

	@Test
	public void notNullAtRuntimeIsNotNull() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "notNullRuntime",
				"something");
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void notNullAnnotationIsNull() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "notNullAnnotation",
				null);
		Assert.assertFalse(violations.isEmpty());
	}

	@Test
	public void notNullAnnotationIsNotNull() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "notNullAnnotation",
				"something");
		Assert.assertTrue(violations.isEmpty());
	}
}
