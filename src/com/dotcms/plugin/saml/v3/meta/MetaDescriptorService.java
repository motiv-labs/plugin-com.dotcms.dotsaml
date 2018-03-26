package com.dotcms.plugin.saml.v3.meta;

import com.dotcms.plugin.saml.v3.config.Configuration;
import com.dotcms.plugin.saml.v3.config.SiteConfigurationBean;

import org.opensaml.saml.saml2.metadata.EntityDescriptor;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Encapsulates the Idp Meta Data xml parsing. Generates the Service Provider
 * Meta Data xml.
 *
 * @author jsanca
 */
public interface MetaDescriptorService extends Serializable
{
	String X_509 = "X.509";

	/**
	 * Parse the meta data xml encapsulate on the inputStream this is to parse
	 * the idp-metadata.
	 * 
	 * @param inputStream
	 *            {@link InputStream} this is the stream of the Idp-metadata.xml
	 * @return MetadataBean
	 * @throws Exception
	 */
	MetadataBean parse( InputStream inputStream, final SiteConfigurationBean siteConfigurationBean ) throws Exception;

	/**
	 * Get the Service Provider Entity Descriptor. This object is built based on
	 * the runtime information configured for the dotCMS SP (Service Provider)
	 * 
	 * @param configuration
	 *            {@link Configuration}
	 * @return EntityDescriptor
	 */
	EntityDescriptor getServiceProviderEntityDescriptor( final Configuration configuration );
}
