package com.swiftelan.validation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ConfigurationState;
import javax.validation.spi.ValidationProvider;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.hibernate.validator.cfg.defs.SizeDef;

import com.swiftelan.validation.spi.PropertyConfigurationProvider;

public class RuntimeValidationProvider implements ValidationProvider<HibernateValidatorConfiguration> {
	private HibernateValidator delegate;

	public RuntimeValidationProvider() {
		delegate = new HibernateValidator();
	}

	@Override
	public HibernateValidatorConfiguration createSpecializedConfiguration(BootstrapState state) {
		HibernateValidatorConfiguration configuration = delegate.createSpecializedConfiguration(state);
		buildRuntimeConfiguration(configuration);
		return configuration;
	}

	void buildRuntimeConfiguration(HibernateValidatorConfiguration configuration) {
		ConstraintMapping constraintMapping = configuration.createConstraintMapping();
		ServiceLoader<PropertyConfigurationProvider> loader = ServiceLoader.load(PropertyConfigurationProvider.class);
		Iterator<PropertyConfigurationProvider> iterator = loader.iterator();
		Map<Class<?>, TypeConstraintMappingContext<?>> typeConstraints = new HashMap<>();
		while (iterator.hasNext()) {
			PropertyConfigurationProvider provider = iterator.next();
			Set<PropertyConfiguration> propertyConfigurations = provider.getPropertyConfigurations();
			if (propertyConfigurations != null) {
				for (PropertyConfiguration config : propertyConfigurations) {
					TypeConstraintMappingContext<?> typeContext = typeConstraints.get(config.getClazz());
					if (typeContext == null) {
						typeContext = constraintMapping.type(config.getClazz());
						typeConstraints.put(config.getClazz(), typeContext);
					}
					PropertyConstraintMappingContext propertyConstraint = typeContext.property(config.getPropertyName(),
							ElementType.FIELD);
					propertyConstraint.ignoreAnnotations();
					if (config.isRequired()) {
						propertyConstraint.constraint(new NotNullDef());
					}

					SizeDef s = new SizeDef();
					if (config.getMin() != null) {
						s.min(config.getMin().intValue());
					}
					if (config.getMax() != null) {
						s.max(config.getMax().intValue());
					}
					if (config.getMin() != null || config.getMax() != null) {
						propertyConstraint.constraint(s);
					}

				}
			}
		}
		configuration.addMapping(constraintMapping);
	}

	@SuppressWarnings("unchecked")
	static <T extends Annotation> ConstraintDescriptor<T> getConstraintDescriptor(Validator validator, Class<?> clazz,
			String propertyName, Class<T> annotationType) {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass(clazz);

		Set<ConstraintDescriptor<?>> constraintDescriptors;
		if (propertyName == null) {
			constraintDescriptors = beanDescriptor.getConstraintDescriptors();
		} else {
			PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty(propertyName);
			if (propertyDescriptor == null) {
				return null;
			}
			constraintDescriptors = propertyDescriptor.getConstraintDescriptors();
		}

		for (ConstraintDescriptor<?> constraint : constraintDescriptors) {
			if (constraint.getAnnotation().annotationType().equals(annotationType)) {
				return (ConstraintDescriptor<T>) constraint;
			}
		}

		return null;
	}

	@Override
	public Configuration<?> createGenericConfiguration(BootstrapState state) {
		Configuration<?> configuration = delegate.createGenericConfiguration(state);
		if (configuration instanceof HibernateValidatorConfiguration) {
			buildRuntimeConfiguration((HibernateValidatorConfiguration) configuration);
		}
		return configuration;
	}

	@Override
	public ValidatorFactory buildValidatorFactory(ConfigurationState configurationState) {
		return delegate.buildValidatorFactory(configurationState);
	}

}
