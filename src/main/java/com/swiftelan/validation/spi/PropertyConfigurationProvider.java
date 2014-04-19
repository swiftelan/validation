package com.swiftelan.validation.spi;

import java.util.Set;

import com.swiftelan.validation.PropertyConfiguration;

public interface PropertyConfigurationProvider {
	Set<PropertyConfiguration> getPropertyConfigurations();
}
