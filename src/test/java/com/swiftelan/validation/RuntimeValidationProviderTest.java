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

	@Test
	public void allowNullOverride() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "allowNull", null);
		Assert.assertFalse(violations.isEmpty());
	}

	@Test
	public void sizeMinNotMet() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "size", "one");
		Assert.assertFalse(violations.isEmpty());
	}

	@Test
	public void sizeMin() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "size", "fives");
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void sizeMax() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "size", "0123456789");
		Assert.assertTrue(violations.isEmpty());
	}

	@Test
	public void sizeMaxNotMet() {
		Set<ConstraintViolation<TestObject>> violations = validator.validateValue(TestObject.class, "size", "01234567890");
		Assert.assertFalse(violations.isEmpty());
	}
}
