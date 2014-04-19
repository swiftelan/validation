package com.swiftelan.validation;

import java.util.HashSet;
import java.util.Set;

import com.swiftelan.validation.spi.PropertyConfigurationProvider;

public class TestPropertyConfigurationProvider implements PropertyConfigurationProvider {

	@Override
	public Set<PropertyConfiguration> getPropertyConfigurations() {
		Set<PropertyConfiguration> properties = new HashSet<>();
		PropertyConfiguration notNullAtRuntime = new PropertyConfiguration();
		notNullAtRuntime.setRequired(true);
		notNullAtRuntime.setClazz(TestObject.class);
		notNullAtRuntime.setPropertyName("notNullRuntime");
		properties.add(notNullAtRuntime);
		return properties;
	}

}
