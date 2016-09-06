# plugin-dotcms-openSAML3

This plugin allows to modify the authentication process in DOTCMS
using the Open SAML 3 (Security Assertion Markup Language) protocols for
frontend, backend or both.

The plugin will add the user in dotcms if the user doens't exist
and everytime the user log the ROLE will be reasigned if the roles 
are sent by the SAML response message.

the SAML Response should always send the user email, firstname and
lastname. The roles are optional


########################################
##  CONFIGURATION
########################################

1) To enable or disable in the plugin you have to include the following servlet on your web.xml (dotCMS app)
before the AutoLoginFilter.

~~~
<filter>
		<filter-name>SamlAccessFilter</filter-name>
		<filter-class>com.dotcms.plugin.saml.v3.filter.SamlAccessFilter</filter-class>
</filter>
~~~

and of course the mapping:

~~~
<filter-mapping>
		<filter-name>SamlAccessFilter</filter-name>
		<url-pattern>/*</url-pattern>
</filter-mapping>
~~~

2) Set in the DOTCMS_plugin_path/conf/dotmarketing-config-ext.properties file the
configuration values for your service provider. The Plugin included some examples, however
you can take a look to DOTCMS_plugin_path/src/com/dotcms/plugin/saml/v3/DotSamlConstants.java, there you can find
all the properties you can override in the dot CMS properties (we will explain all of them later).

3) By default we have included the SPKeystore.jks, however you should use/create your own key store file.
You should take in consideration that the keystore should have a certificate. Here you
could see and example of how you can create one
http://blog.tirasa.net/category/codeexp/security/create-a-new-keystore-to.html

In addition here is an example of the properties to override:

dotcms.saml.keystore.path=SPKeystore.jks
dotcms.saml.keystore.password=password
dotcms.saml.keyentryid=SPKey
dotcms.saml.keystore.entry.password=password

Keep in mind that the dotcms.saml.keystore.path, could be get from the app classpath or from the file system;
To include a file system just include the prefix file://

For instance:
dotcms.saml.keystore.path=file:///opt/keystores/myKeystore.jks

4) Setting up more configuration:

4.1) dotcms.saml.protocol.binding

By default dotCMS used org.opensaml.saml.common.xml.SAMLConstants.SAML2_ARTIFACT_BINDING_URI, probably you do not need to change it but if you can override it here if needed.

4.2) dotcms.saml.identity.provider.destinationsso.url

This is url for the login page on the OpenSAML Server, by default it gets url from the idp-metadata (the file provided from the OpenSAML server), but if it is not any idp-metadata you can
edit this property and include the SSO url. (Note, if you set this property and set the idp-metadata, the idp-metada will be get by default)

4.3) dotcms.saml.artifact.resolution.service.url

This is a mandatory property for the app and it is the SOAP URL for the Artifact Resolution Service (the one that gets the user information, the Assertion).

4.4) dotcms.saml.assertion.customer.endpoint.url

This is the URL where the Idp (the OpenSAML server) will be redirected to dotCMS when the login is made, we suggest to go to http://[domain]/c.
If this value is not set, will be send a current request as a default, however keep in mind some Idp Server might not admit this configuration.

4.5) dotcms.saml.service.provider.issuer

This is the App Id for the DotCMS Service Provider, by default we use this one: "com.dotcms.plugin.saml.v3.issuer", we recommend to use you url.com address, for instance:

http://www.dotcms.com, could be the dotCMS id.

4.6) dotcms.saml.policy.allowcreate
By default dotCMS plugin advise to not allow to create new user on the Idp, however you can advise the value you want (true or false) overriding the value in the properties file.

4.7) dotcms.saml.policy.format
By default we support TRANSIENT and PERSISTANCE formats, however if you want to override it just add the values (comma separated) in the properties file.
See org.opensaml.saml.saml2.core.NameIDType for more details about the valid values.

4.8) dotcms.saml.authn.comparisontype
By default we use a MINIMUM Authorization, But you can switch to another one; for instance:

dotcms.saml.authn.comparisontype=BETTER

MINIMUM
The user could be authenticated by using password or any stronger method, such as smart card for instance.

BETTER
The user must be authenticated with a stronger method than password.

EXACT
The user will be authenticated with a specific method assigment for it, for instance if it is password, the user will be authenticated by password, not anything else.

MAXIMUM
The user will use the strong possible method.

4.9) dotcms.saml.authn.context.class.ref

This is the authentication context, it could be Kerberos, it could be Internet protocol, password, etc. See org.opensaml.saml.saml2.core.AuthnContext for more details.
By default we use: org.opensaml.saml.saml2.core.AuthnContext.PASSWORD_AUTHN_CTX

4.10) dotcms.saml.keystore.path

Class path or file system path for the key store, we have a dummy KeyStore called SPKeystore.jks on the plugin however it is highly recommend to create/use your own store.

4.11) dotcms.saml.keystore.password

Password to access the key store

4.12) dotcms.saml.keyentryid

This is the key entry for the key store, by default we use SPKey, you can override it if needed.

4.13) dotcms.saml.keystore.entry.password

This is the key entry password for the key store, by default we use "password", you can override it if needed.

4.14) dotcms.saml.keystore.type

By default dotCMS use java.security.KeyStore.getDefaultType(), however if you key store is a type different, you can override it here.

4.15) dotcms.saml.signature.canonicalization.algorithm

By default we use  org.opensaml.xmlsec.signature.support.SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, you can override it if needed.

4.16) dotcms.saml.clock.skew and dotcms.saml.message.life.time

DotCMS does validation for the message lifetime, by default the clock skew is 1000 and life time 2000, in case you need a greater value feel free to override it.


4.17) dotcms.saml.remove.roles.prefix

Depending on your Identity providers on the IdP, the roles may be returned on the assertion with a prefix, you can remove it by setting it on the dotCMS properties.

4.18) dotcms.saml.email.attribute

By default "mail" is the field used to fetch the user email from the Idp response, however if you are using another one you can override it on the properties.

4.19) dotcms.saml.firstname.attribute

By default "givenName" is the field used to fetch the user name from the Idp response, however if you are using another one you can override it on the properties.

4.20) dotcms.saml.lastname.attribute

By default "sn" is the field used to fetch the last name from the Idp response, however if you are using another one you can override it on the properties.

4.21) dotcms.saml.roles.attribute

By default "authorisations" is the field used to fetch the roles/groups from the Idp response, however if you are using another one you can override it on the properties.

4.22) dotcms.saml.initializer.classname

By default dotcms use: DefaultInitializer it inits the Java Crypto, Saml Services and plugin stuff.
However if you have a custom implementation of Initializer, you can override by adding a full class name to this property.

4.23) dotcms.saml.configuration.classname

By default we use com.dotcms.plugin.saml.v3.config.DefaultDotCMSConfiguration to handle the plugin configuration,
However if you have a custom implementation of Configuration, you can override by adding a full class name to this property.

4.24) dotcms.saml.idp.metadata.path

In case you have a idp-metadata.xml you can get it from the classpath or file system.
For the classpath you overrides the property with the right path in your class path.
If you want to get the XML from the file system use the prefix; file://

4.25) dotcms.saml.idp.metadata.protocol

This is the attribute name to find the Idp Information on the idp-metadata.xml (the file provided from the OpenSAML server), the default used is
"urn:oasis:names:tc:SAML:2.0:protocol", probably you do not need to change it but if you can override it here if needed.

4.26) dotcms.saml.idp.metadata.parser.classname

By default dotCMS use DefaultMetaDescriptorServiceImpl, this class parse the idp-metadata and creates the sp-metadata from the runtime information.
However if you have a custom implementation of MetaDescriptorService, you can override by adding a full class name to this property.

4.27) dotcms.saml.access.filter.values

By default dotCMS does not filter any url, however if you want to avoid to check open saml authentication over any URL please add (comma separated) the list of
urls on the properties file.

4.28) dotcms.saml.service.provider.custom.credential.provider.classname

In case you need a custom credentials for the Service Provider (DotCMS) overrides the implementation class on the configuration properties.
Please see com.dotcms.plugin.saml.v3.CredentialProvider

4.29) dotcms.saml.id.provider.custom.credential.provider.classname

In case you need a custom credentials for the ID Provider (DotCMS) overrides the implementation class on the configuration properties.
Please see com.dotcms.plugin.saml.v3.CredentialProvider

4.30) dotcms.saml.want.assertions.signed

By default true, overrides it if you want the assertions signed or not (true or false).

4.31) dotcms.saml.authn.requests.signed

By default true, overrides it if you want the authorization requests signed or not (true or false).

4.32) dotcms.saml.sevice.provider.custom.metadata.path

By default this is the URL to get the dotCMS Service Provider metadata: "/dotsaml3sp/metadata.xml"
However if you want to use a different path, feel free to override it on the properties file.

########################################
##  HOW TO USE
########################################

To use the plugin run the ./bin/deploy-plugins.sh command and restart your 
dotCMS instance.

To see your service provider metadata by default generated by the plugin use this url
https://<myhost>/dotsaml3sp/metadata.xml
However you can override it on the DOTCMS_plugin_path/conf/dotmarketing-config-ext.properties,
using the property: "dotcms.saml.sevice.provider.custom.metadata.path"

Any request on DotCMS will be redirect to the IdP Login Page, if the user is not already login.
The rule exception will the url's set on DOTCMS_plugin_path/conf/dotmarketing-config-ext.properties, with the property: "dotcms.saml.access.filter.values"
