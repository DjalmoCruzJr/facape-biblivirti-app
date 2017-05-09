package org.sysmob.biblivirti.utils;

/**
 * @author djalmocruzjr
 * @version 1.0
 * @since 22/12/2016
 * <p>
 * Classe para armazenar todas as constantes utilizadas na aplicacao.
 * </p>
 */
public abstract class BiblivirtiConstants {

    /**
     * Constantes relacionadas ao modo de apresentacao dos dados
     */
    public static final String ACTIVITY_TITLE = "activity_title";
    public static final String ACTIVITY_MODE_KEY = "activity_mode";
    public static final int ACTIVITY_MODE_INSERTING = 0;
    public static final int ACTIVITY_MODE_EDITING = 1;

    /**
     * Constantes relacionadas a tela SplachActivity
     */
    public static final int ACTIVITY_SPLASH_TIME_OUT = 3000;

    /**
     * Constantes que definem a localizacao da API de servicos
     */
    public static final String SERVER_HOST = "192.168.254.16";
    public static final int SERVER_PORT = 8081;
    public static final String SERVER_API = "http://" + SERVER_HOST + ":" + SERVER_PORT + "/projetos/sysmob/biblivirti/API";

    /**
     * Constantes que definem as configuracoes de uso da API do Facebook
     */
    public static final String FACEBOOK_APP_ID = "1589075548038907";
    public static final String FACEBOOK_APP_SECRET_KEY = "22ec6813c95bb486173f2752a28068a7";

    /**
     * Constantes que definem as configuracoes de uso da API do Firebase (Google)
     */
    public static final String FIREBASE_PROJECT_ID = "biblivirti";
    public static final String FIREBASE_WEB_API_KEY = "AIzaSyA4k4h0qRZfTPR3lkp6ROZRu336CSgMzSw";
    public static final String FIREBASE_APP_ID = "1:1025303689282:android:e79a3de37e2db2fc";
    public static final String FIREBASE_SERVER_KEY = "AAAA7rjcuEI:APA91bEMtM1TrKcAAFF8bQugzyziIH-qbvQQyddB_CfovhpWF1s-cy4Q8LHcCz9kZW8JWxGPLzT-tZdT6u7Pr7pO9sTbK0A2CHpzTrl_PNbseE_Mvh7yLxQa3oQu2EBVt0zOBBPBWySf";
    public static final String FIREBASE_SENDER_KEY = "1025303689282";

    /**
     * Constantes que definem as URL's dos servicos de ACCOUNT da API
     */
    public static final String API_ACCOUNT_LOGIN = SERVER_API + "/account/login";
    public static final String API_ACCOUNT_REGISTER = SERVER_API + "/account/register";
    public static final String API_ACCOUNT_RECOVERY = SERVER_API + "/account/recovery";
    public static final String API_ACCOUNT_EMAIL_CONFIRMATION = SERVER_API + "/account/email/confirmation";
    public static final String API_ACCOUNT_PASSWORD_RESET = SERVER_API + "/account/password/reset";
    public static final String API_ACCOUNT_PASSWORD_EDIT = SERVER_API + "/account/password/edit";
    public static final String API_ACCOUNT_SEARCH = SERVER_API + "/account/search";

    /**
     * Constantes que definem as URL's dos servicos de AREAOFINTEREST da API
     */
    public static final String API_AREAOFINTEREST_LIST = SERVER_API + "/areaofinterest/list";
    public static final String API_AREAOFINTEREST_ADD = SERVER_API + "/areaofinterest/add";

    /**
     * Constantes que definem as URL's dos servicos de CONTENT da API
     */
    public static final String API_CONTENT_LIST = SERVER_API + "/content/list";
    public static final String API_CONTENT_ADD = SERVER_API + "/content/add";
    public static final String API_CONTENT_EDIT = SERVER_API + "/content/edit";

    /**
     * Constantes que definem as URL's dos servicos de GROUP da API
     */
    public static final String API_GROUP_LIST = SERVER_API + "/group/list";
    public static final String API_GROUP_GET = SERVER_API + "/group/get";
    public static final String API_GROUP_ADD = SERVER_API + "/group/add";
    public static final String API_GROUP_EDIT = SERVER_API + "/group/edit";
    public static final String API_GROUP_DELETE = SERVER_API + "/group/delete";
    public static final String API_GROUP_INFO = SERVER_API + "/group/info";
    public static final String API_GROUP_SEARCH = SERVER_API + "/group/search";
    public static final String API_GROUP_SUBSCRIBE = SERVER_API + "/group/subscribe";
    public static final String API_GROUP_UNSUBSCRIBE = SERVER_API + "/group/unsubscribe";

    /**
     * Constantes que definem as URL's dos servicos de MATERIAL da API
     */
    public static final String API_MATERIAL_LIST = SERVER_API + "/material/list";
    public static final String API_MATERIAL_ADD = SERVER_API + "/material/add";
    public static final String API_MATERIAL_EDIT = SERVER_API + "/material/edit";
    public static final String API_MATERIAL_DELETE = SERVER_API + "/material/delete";
    public static final String API_MATERIAL_SEARCH = SERVER_API + "/material/search";
    public static final String API_MATERIAL_SHARE = SERVER_API + "/material/share";
    public static final String API_MATERIAL_EMAIL = SERVER_API + "/material/email";
    public static final String API_MATERIAL_DETAILS = SERVER_API + "/material/details";

    /**
     * Constantes que definem as URL's dos servicos de MESSAGE da API
     */
    public static final String API_MESSAGE_LIST = SERVER_API + "/message/list";
    public static final String API_MESSAGE_ADD = SERVER_API + "/message/add";

    /**
     * Constantes que definem as URL's dos servicos de COMMENT da API
     */
    public static final String API_COMMENT_ADD = SERVER_API + "/comment/add";
    public static final String API_COMMENT_EDIT = SERVER_API + "/comment/edit";
    public static final String API_COMMENT_DELETE = SERVER_API + "/comment/delete";

    /**
     * Constantes que definem as URL's dos servicos de QUESTION da API
     */
    public static final String API_QUESTION_LIST = SERVER_API + "/question/list";
    public static final String API_QUESTION_ADD = SERVER_API + "/question/add";
    public static final String API_QUESTION_EDIT = SERVER_API + "/question/edit";
    public static final String API_QUESTION_DELETE = SERVER_API + "/question/delete";

    /**
     * Constantes que definem as URL's dos servicos de ALTERNATIVE da API
     */
    public static final String API_ALTERNATIVE_LIST = SERVER_API + "/alternative/list";
    public static final String API_ALTERNATIVE_EDIT = SERVER_API + "/alternative/edit";
    public static final String API_ALTERNATIVE_DELETE = SERVER_API + "/alternative/delete";

    /**
     * Constantes que definem as URL's dos servicos de ANSWER da API
     */
    public static final String API_ANSWER_LIST = SERVER_API + "/answer/list";
    public static final String API_ANSWER_EDIT = SERVER_API + "/answer/edit";
    public static final String API_ANSWER_DELETE = SERVER_API + "/answer/delete";

    /**
     * Constantes que definem as URL's dos servicos de DOUBT da API
     */
    public static final String API_DOUBT_LIST = SERVER_API + "/doubt/list";
    public static final String API_DOUBT_ADD = SERVER_API + "/doubt/add";
    public static final String API_DOUBT_EDIT = SERVER_API + "/doubt/edit";
    public static final String API_DOUBT_DELETE = SERVER_API + "/doubt/delete";
    public static final String API_DOUBT_SHARE = SERVER_API + "/doubt/share";
    public static final String API_DOUBT_EMAIL = SERVER_API + "/doubt/email";
    public static final String API_DOUBT_DETAILS = SERVER_API + "/doubt/details";
    public static final String API_DOUBT_SEARCH = SERVER_API + "/doubt/search";

    /**
     * Constantes que definem as URL's dos servicos de DOUBTANSWER da API
     */
    public static final String API_DOUBTANSWER_ADD = SERVER_API + "/doubtanswer/add";
    public static final String API_DOUBTANSWER_EDIT = SERVER_API + "/doubtanswer/edit";
    public static final String API_DOUBTANSWER_DELETE = SERVER_API + "/doubtanswer/delete";
    public static final String API_DOUBTANSWER_EMAIL = SERVER_API + "/doubtanswer/email";
    public static final String API_DOUBTANSWER_DETAILS = SERVER_API + "/doubtanswer/details";
    public static final String API_DOUBTANSWER_LIST = SERVER_API + "/doubtanswer/list";

    /**
     * Constantes relacionadas com os campos da respostas das requisicoes
     */
    public static final String RESPONSE_CODE = "response_code";
    public static final String RESPONSE_MESSAGE = "response_message";
    public static final String RESPONSE_DATA = "response_data";
    public static final String RESPONSE_ERRORS = "response_errors";
    public static final int RESPONSE_CODE_OK = 200;
    public static final int RESPONSE_CODE_NOT_FOUND = 404;
    public static final int RESPONSE_CODE_BAD_REQUEST = 400;
    public static final int RESPONSE_CODE_UNAUTHORIZED = 401;

    /**
     * Constantes relacionadas as requisicoes a API
     */
    public static final int REQUEST_TIMEOUT = 10000;
    public static final int DEFAULT_MAX_RETRIES = 0;
    public static final float DEFAULT_BACKOFF_MULT = 0;

    /**
     * Constantes relacioandas com o gerenciamento de preferences do aplicativo
     */
    public static final String PREFERENCE_FILE_NAME = "BIBLIVIRTI_PREFERENCES";
    public static final String PREFERENCE_PROPERTY_EMAIL = "org.sysmob.biblivirti.PREFERENCE_PROPERTY_EMAIL";
    public static final String PREFERENCE_PROPERTY_SENHA = "org.sysmob.biblivirti.PREFERENCE_PROPERTY_SENHA";

    /**
     * Outras constantes utilizadas no projeto
     */
    public static final String FIELD_SEARCH_REFERENCE = "reference";
}
