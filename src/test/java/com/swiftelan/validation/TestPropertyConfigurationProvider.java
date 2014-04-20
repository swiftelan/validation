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

		PropertyConfiguration allowNull = new PropertyConfiguration();
		allowNull.setRequired(true);
		allowNull.setClazz(TestObject.class);
		allowNull.setPropertyName("allowNull");
		properties.add(allowNull);

		PropertyConfiguration size = new PropertyConfiguration();
		size.setRequired(false);
		size.setClazz(TestObject.class);
		size.setPropertyName("size");
		size.setMin(Integer.valueOf(5));
		size.setMax(Integer.valueOf(10));
		properties.add(size);

		return properties;
	}

}
