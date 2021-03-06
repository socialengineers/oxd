/**
 * All rights reserved -- Copyright 2015 Gluu Inc.
 */
package org.xdi.oxd.server;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * oxD configuration.
 *
 * @author Yuriy Zabrovarnyy
 */
public class Configuration {

    public static final String DOC_URL = "https://www.gluu.org/docs-oxd/";

    @JsonProperty(value = "port")
    private int port;
    @JsonProperty(value = "time_out_in_seconds")
    private int timeOutInSeconds;
    //@JsonProperty(value = "register_client_app_type")
    private String registerClientAppType = "web";
    //    @JsonProperty(value = "register_client_response_types")
    private String registerClientResponesType = "code";
    @JsonProperty(value = "server_name")
    private String serverName;
    @JsonProperty(value = "localhost_only")
    private Boolean localhostOnly;
    @JsonProperty(value = "use_client_authentication_for_pat")
    private Boolean useClientAuthenticationForPat = true;
    @JsonProperty(value = "trust_all_certs")
    private Boolean trustAllCerts;
    @JsonProperty(value = "trust_store_path")
    private String keyStorePath;
    @JsonProperty(value = "trust_store_password")
    private String keyStorePassword;
    @JsonProperty(value = "license_id")
    private String licenseId;
    @JsonProperty(value = "public_key")
    private String publicKey;
    @JsonProperty(value = "public_password")
    private String publicPassword;
    @JsonProperty(value = "license_password")
    private String licensePassword;
    @JsonProperty(value = "support-google-logout")
    private Boolean supportGoogleLogout = true;
    @JsonProperty(value = "state_expiration_in_minutes")
    private int stateExpirationInMinutes = 5;
    @JsonProperty(value = "nonce_expiration_in_minutes")
    private int nonceExpirationInMinutes = 5;
    @JsonProperty(value = "public_op_key_cache_expiration_in_minutes")
    private int publicOpKeyCacheExpirationInMinutes = 60;
    @JsonProperty(value = "protect_commands_with_access_token")
    private Boolean protectCommandsWithAccessToken;
    @JsonProperty(value = "uma2_auto_register_claims_gathering_endpoint_as_redirect_uri_of_client")
    private Boolean uma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient;
    @JsonProperty(value = "migration_source_folder_path")
    private String migrationSourceFolderPath;
    @JsonProperty(value = "storage")
    private String storage;
    @JsonProperty(value = "storage_configuration")
    private JsonNode storageConfiguration;

    public String getMigrationSourceFolderPath() {
        return migrationSourceFolderPath;
    }

    public void setMigrationSourceFolderPath(String migrationSourceFolderPath) {
        this.migrationSourceFolderPath = migrationSourceFolderPath;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Boolean getUma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient() {
        return uma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient;
    }

    public void setUma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient(Boolean uma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient) {
        this.uma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient = uma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient;
    }

    public int getStateExpirationInMinutes() {
        return stateExpirationInMinutes;
    }

    public void setStateExpirationInMinutes(int stateExpirationInMinutes) {
        this.stateExpirationInMinutes = stateExpirationInMinutes;
    }

    public Boolean getProtectCommandsWithAccessToken() {
        return protectCommandsWithAccessToken;
    }

    public void setProtectCommandsWithAccessToken(Boolean protectCommandsWithAccessToken) {
        this.protectCommandsWithAccessToken = protectCommandsWithAccessToken;
    }

    public int getPublicOpKeyCacheExpirationInMinutes() {
        return publicOpKeyCacheExpirationInMinutes;
    }

    public void setPublicOpKeyCacheExpirationInMinutes(int publicOpKeyCacheExpirationInMinutes) {
        this.publicOpKeyCacheExpirationInMinutes = publicOpKeyCacheExpirationInMinutes;
    }

    public int getNonceExpirationInMinutes() {
        return nonceExpirationInMinutes;
    }

    public void setNonceExpirationInMinutes(int nonceExpirationInMinutes) {
        this.nonceExpirationInMinutes = nonceExpirationInMinutes;
    }

    public Boolean getSupportGoogleLogout() {
        return supportGoogleLogout;
    }

    public void setSupportGoogleLogout(Boolean supportGoogleLogout) {
        this.supportGoogleLogout = supportGoogleLogout;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public String getLicensePassword() {
        return licensePassword;
    }

    public void setLicensePassword(String licensePassword) {
        this.licensePassword = licensePassword;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicPassword() {
        return publicPassword;
    }

    public void setPublicPassword(String publicPassword) {
        this.publicPassword = publicPassword;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public Boolean getTrustAllCerts() {
        return trustAllCerts;
    }

    public void setTrustAllCerts(Boolean trustAllCerts) {
        this.trustAllCerts = trustAllCerts;
    }

    public Boolean getUseClientAuthenticationForPat() {
        return useClientAuthenticationForPat;
    }

    public void setUseClientAuthenticationForPat(Boolean useClientAuthenticationForPat) {
        this.useClientAuthenticationForPat = useClientAuthenticationForPat;
    }

    public Boolean getLocalhostOnly() {
        return localhostOnly;
    }

    public void setLocalhostOnly(Boolean p_localhostOnly) {
        localhostOnly = p_localhostOnly;
    }

    public String getRegisterClientResponesType() {
        return registerClientResponesType;
    }

    public void setRegisterClientResponesType(String p_registerClientResponesType) {
        registerClientResponesType = p_registerClientResponesType;
    }

    public String getRegisterClientAppType() {
        return registerClientAppType;
    }

    public void setRegisterClientAppType(String p_registerClientAppType) {
        registerClientAppType = p_registerClientAppType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(int timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public JsonNode getStorageConfiguration() {
        return storageConfiguration;
    }

    public void setStorageConfiguration(JsonNode storageConfiguration) {
        this.storageConfiguration = storageConfiguration;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "port=" + port +
                ", timeOutInSeconds=" + timeOutInSeconds +
                ", registerClientAppType='" + registerClientAppType + '\'' +
                ", registerClientResponesType='" + registerClientResponesType + '\'' +
                ", serverName='" + serverName + '\'' +
                ", localhostOnly=" + localhostOnly +
                ", useClientAuthenticationForPat=" + useClientAuthenticationForPat +
                ", trustAllCerts=" + trustAllCerts +
                ", keyStorePath='" + keyStorePath + '\'' +
                ", keyStorePassword='" + keyStorePassword + '\'' +
                ", licenseId='" + licenseId + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", publicPassword='" + publicPassword + '\'' +
                ", licensePassword='" + licensePassword + '\'' +
                ", supportGoogleLogout=" + supportGoogleLogout +
                ", stateExpirationInMinutes=" + stateExpirationInMinutes +
                ", nonceExpirationInMinutes=" + nonceExpirationInMinutes +
                ", publicOpKeyCacheExpirationInMinutes=" + publicOpKeyCacheExpirationInMinutes +
                ", protectCommandsWithAccessToken=" + protectCommandsWithAccessToken +
                ", uma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient=" + uma2AuthRegisterClaimsGatheringEndpointAsRedirectUriOfClient +
                ", migrationSourceFolderPath='" + migrationSourceFolderPath + '\'' +
                ", storage='" + storage + '\'' +
                ", storageConfiguration='" + storageConfiguration + '\'' +
                '}';
    }
}
