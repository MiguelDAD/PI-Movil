package com.example.pi_movil.comunicacion;

public class Mensajes {

    // USUARIOS
    /* LOGIN - REGISTER */
    public static final String LOGIN = "CLI-LOGIN";
    public static final String REGISTER = "CLI-REGISTER";

    /* LOGIN DE ORGANIZADOR */
    public static final String LOGIN_ORGANIZER = "CLI-LOGIN-ORGANIZER";
    public static final String LOGIN_ACCEPT_ORGANIZER = "SEV-LOGIN-OK-ORGANIZER";
    public static final String LOGIN_ACCEPT_ADMIN = "SEV-LOGIN-OK-ADMIN";
    public static final String LOGIN_ERROR_ORGANIZER = "SER-LOGIN-ERROR-ORGANIZER";

    /* LOGIN DE USUARIO */
    public static final String LOGIN_USER = "CLI-LOGIN-USER";
    public static final String LOGIN_ACCEPT_USER = "SEV-LOGIN-OK-USER";
    public static final String LOGIN_ERROR_USER = "SER-LOGIN-ERROR-USER";

    /* REGISTER DE ORGANIZADOR */
    public static final String REGISTER_ORGANIZER = "CLI-REGISTER-ORGANIZER";
    public static final String REGISTER_ACCEPT_ORGANIZER = "SEV-REGISTER-OK-ORGANIZER";
    public static final String REGISTER_ERROR_ORGANIZER = "SEV-REGISTER-ERROR-ORGANIZER";

    /* REGISTER DE USUARIO */
    public static final String REGISTER_USER = "CLI-REGISTER-ORGANIZER";
    public static final String REGISTER_ACCEPT_USER = "SEV-REGISTER-OK-ORGANIZER";
    public static final String REGISTER_ERROR_USER = "SEV-REGISTER-ERROR-ORGANIZER";

    /* SALIR */
    public static final String EXIT = "CLI-EXIT";
    public static final String LOGOUT = "CLI-LOGOUT";
    public static final String LOGOUT_SEND = "SEV-LOGOUT-OK";

    // DATOS
    /* OBTENER DEPORTES DISPONIBLES */
    public static final String GET_SPORT = "CLI-GIVE-SPORTS";
    public static final String SEND_SPORT = "SEV-SEND-SPORTS";
    public static final String ERROR_SEND_SPORT = "SEV-ERROR-SEND-SPORTS";

    // TORNEOS
    /* TORNEOS DE UN ORGANIZADOR */
    public static final String TOURNAMENTS_ORGANIZER = "CLI-GIVETOURNAMENTS-ORGANIZER";
    public static final String TOURNAMENTS_SEND_ORGANIZER = "SEV-SEND-YOUR-TOURNAMENTS";
    public static final String TOURNAMENTS_ERROR_SEND_ORGANIZER = "SEV-ERROR-SEND-YOUR-TOURNAMENTS";

    /* CREAR TORNEO */
    public static final String TOURNAMENT_CREATE = "CLI-CREATETOURNAMENT";
    public static final String TOURNAMENT_OK_CREATE = "SEV-TOURNAMENT-CREATE";
    public static final String TOURNAMENT_ERROR_CREATE = "SEV-TOURNAMENT-CREATE-ERROR";

    /* OBTENER EQUIPOS DE UN TORNEO */
    public static final String TOURNAMENT_TEAMS = "CLI-TEAMSTOURNAMENT";
    public static final String TOURNAMENT_TEAMS_OK = "SEV-YOURTEAMSTOURNAMENT";
    public static final String TOURNAMENT_TEAMS_ERROR = "SEV-ERRORTEAMSTOURNAMENT";

    // LIGAS
    /* LIGAS DE UN ORGANIZADOR */
    public static final String LIGUES_ORGANIZER = "CLI-GIVELIGUES-ORGANIZER";
    public static final String LIGUES_SEND_ORGANIZER = "SEV-SEND-YOUR-LIGUES";
    public static final String LIGUES_ERROR_SEND_ORGANIZER = "SEV-ERROR-SEND-YOUR-LIGUES";

    /* CREAR LIGA */
    public static final String LIGUES_CREATE = "CLI-CREATELIGUE";
    public static final String LIGUES_OK_CREATE = "SEV-LIGUE-CREATE";
    public static final String LIGUES_ERROR_CREATE = "SEV-LIGUE-CREATE-ERROR";

    /* OBTENER EQUIPOS DE UNA LIGA */
    public static final String LIGUES_TEAMS = "CLI-TEAMSLIGUE";
    public static final String LIGUES_TEAMS_OK = "SEV-YOURTEAMSLIGUE";
    public static final String LIGUES_TEAMS_ERROR = "SEV-ERRORTEAMSLIGUE";

    // DATOS USUARIO
    public static final String USERS_MYTEAMS = "CLI-USERTEAMS";
    public static final String USERS_MYTEAMS_OK = "SEV-USERTEAMS_OK";
    public static final String USERS_MYTEAMS_ERROR = "SEV-USERTEAMS_ERROR";

    /* AÑADIR USUARIO A EQUIPO */
    public static final String TEAM_INVITE_USER = "CLI-INVITEUSERTEAM";
    public static final String TEAM_INVITE_USER_OK = "SEV-INVITEUSERTEAM-OK";
    public static final String TEAM_INVITE_USER_ERROR = "SEV-INVITEUSERTEAM-ERROR";

    /* CREAR EQUIPO */
    public static final String TEAM_CREATE = "CLI-CREATETEAM";
    public static final String TEAM_CREATE_OK = "SEV-CREATETEAM-OK";
    public static final String TEAM_CREATE_ERROR = "SEV-CREATETEAM-ERROR";

    /* TORNEOS DE UN USUARIO */
    public static final String TOURNAMENTS_USER = "CLI-TOURNAMENTS-USER";
    public static final String TOURNAMENTS_USER_OK = "SEV-TOURNAMENTS-USER-OK";
    public static final String TOURNAMENTS_USER_ERROR = "SEV-TOURNAMENTS-USER-ERROR";

    /* TORNEOS DISPONIBLES */
    public static final String TOURNAMENTS_AVAILABLE = "CLI-TOURNAMENTS-AVAILABLE";
    public static final String TOURNAMENTS_AVAILABLE_OK = "SEV-TOURNAMENTS-AVAILABLE-OK";
    public static final String TOURNAMENTS_AVAILABLE_ERROR = "SEV-TOURNAMENTS-AVAILABLE-ERROR";

    /* TORNEOS ESTADO? */
    public static final String TOURNAMENTS_STATE_USER = "CLI-TOURNAMENTS-STATE-USER";
    public static final String TOURNAMENTS_STATE_USER_OK = "SEV-TOURNAMENTS-STATE-USER-OK";
    public static final String TOURNAMENTS_STATE_USER_ERROR = "SEV-TOURNAMENTS-STATE-USER-ERROR";

    /* EQUIPOS DISPONIBLES PARA UN TORNEO */
    public static final String TOURNAMENTS_TEAMS_INSCRIBE_USER = "CLI-TOURNAMENTS-TEAMS-INSCRIBE-USER";
    public static final String TOURNAMENTS_TEAMS_INSCRIBE_USER_OK = "SEV-TOURNAMENTS-TEAMS-INSCRIBE-USER-OK";
    public static final String TOURNAMENTS_TEAMS_INSCRIBE_USER_ERROR = "SEV-TOURNAMENTS-TEAMS-INSCRIBE-USER-ERROR";

    /* INSCRIBIR EQUIPO A TORNEO */
    public static final String TOURNAMENTS_INSCRIBE_TEAM_USER = "CLI-TOURNAMENTS-INSCRIBE-TEAM-USER";
    public static final String TOURNAMENTS_INSCRIBE_TEAM_USER_OK = "SEV-TOURNAMENTS-INSCRIBE-TEAM-USER-OK";
    public static final String TOURNAMENTS_INSCRIBE_TEAM_USER_ERROR = "SEV-TOURNAMENTS-INSCRIBE-TEAM-USER-ERROR";

    /* RONDAS DE UN TORNEO */
    public static final String TOURNAMENTS_ROUNDS = "CLI-TOURNAMENTS-ROUNDS";
    public static final String TOURNAMENTS_ROUNDS_OK = "SEV-TOURNAMENTS-ROUNDS";
    public static final String TOURNAMENTS_ROUNDS_ERROR = "SEV-TOURNAMENTS-ROUNDS";

    /* GUARDAR RONDAS DE UN TORNEO */
    public static final String SAVE_ROUNDS = "CLI-SAVE-ROUNDS";
    public static final String SAVE_ROUNDS_OK = "SEV-SAVE-ROUNDS-OK";
    public static final String SAVE_ROUNDS_ERROR = "SEV-SAVE-ROUNDS-ERROR";

    /* TERMINAR TORNEO */
    public static final String TOURNAMENTS_END = "CLI-TOURNAMENTS-END";
    public static final String TOURNAMENTS_END_OK = "SEV-TOURNAMENTS-END-OK";
    public static final String TOURNAMENTS_END_ERROR = "SEV-TOURNAMENTS-END-ERROR";

    /* CANCELAR TORNEO */
    public static final String TOURNAMENTS_CANCEL = "CLI-TOURNAMENTS-CANCEL";
    public static final String TOURNAMENTS_CANCEL_OK = "SEV-TOURNAMENTS-CANCEL-OK";
    public static final String TOURNAMENTS_CANCEL_ERROR = "SEV-TOURNAMENTS-CANCEL-ERROR";

    /* PARTIDOS DE UN USUARIO */
    public static final String GAMES_USER = "CLI-GAMES-USER";
    public static final String GAMES_USER_OK = "SEV-GAMES-USER-OK";
    public static final String GAMES_USER_ERROR = "SEV-GAMES-USER-ERROR";

    /* PARTIDOS DISPONIBLES */
    public static final String GAMES_AVAILABLE = "CLI-GAMES-AVAILABLE";
    public static final String GAMES_AVAILABLE_OK = "SEV-GAMES-AVAILABLE-OK";
    public static final String GAMES_AVAILABLE_ERROR = "SEV-GAMES-AVAILABLE-ERROR";

    /* CREAR PARTIDO */
    public static final String GAME_CREATE = "CLI-GAME-CREATE";
    public static final String GAME_CREATE_OK = "SEV-GAME-CREATE-OK";
    public static final String GAME_CREATE_ERROR = "SEV-GAME-CREATE-ERROR";

    /* PARTIDO ESTADO? */
    public static final String GAMES_STATE_USER = "CLI-GAMES-STATE-USER";
    public static final String GAMES_STATE_USER_OK = "SEV-GAMES-STATE-USER-OK";
    public static final String GAMES_STATE_USER_ERROR = "SEV-GAMES-STATE-USER-ERROR";

    /* INSCRIBIR USUARIO A PARTIDO */
    public static final String GAMES_INSCRIBE_USER = "CLI-GAMES_INSCRIBE_USER";
    public static final String GAMES_INSCRIBE_USER_OK = "SEV-GAMES_INSCRIBE_USER-OK";
    public static final String GAMES_INSCRIBE_USER_ERROR = "SEV-GAMES_INSCRIBE_USER-ERROR";

    /* OBTENER CANTIDAD DE JORNADAS */
    public static final String LIGUES_NUMBER_ROUNDS = "CLI-LIGUES-NUMBER-ROUND";
    public static final String LIGUES_NUMBER_ROUNDS_OK = "SEV-LIGUES-NUMBER-ROUND-OK";
    public static final String LIGUES_NUMBER_ROUNDS_ERROR = "SEV-LIGUES-NUMBER-ROUND-ERROR";

    /* OBTENER DATOS DE UNA JORNADA */
    public static final String LIGUES_ROUND_DATE = "CLI-LIGUES-ROUND-DATE";
    public static final String LIGUES_ROUND_DATE_OK = "SEV-LIGUES-ROUND-DATE-OK";
    public static final String LIGUES_ROUND_DATE_ERROR = "SEV-LIGUES-ROUND-DATE-ERROR";

    /* OBTENER DATOS DE UNA JORNADA */
    public static final String LIGUES_JORNATE_SAVE = "CLI-LIGUES-JORNATE-SAVE";
    public static final String LIGUES_JORNATE_SAVE_OK = "SEV-LIGUES-JORNATE-SAVE-OK";
    public static final String LIGUES_JORNATE_SAVE_ERROR = "SEV-LIGUES-JORNATE-SAVE-ERROR";

    /* OBTENER PUNTOS DE UNA LIGA */
    public static final String LIGUES_SCORE = "CLI-LIGUES-SCORE";
    public static final String LIGUES_SCORE_OK = "SEV-LIGUES-SCORE-OK";
    public static final String LIGUES_SCORE_ERROR = "SEV-LIGUES-SCORE-ERROR";

    /* TERMINAR LIGA */
    public static final String LIGUES_END = "CLI-LIGUES-END";
    public static final String LIGUES_END_OK = "SEV-LIGUES-END-OK";
    public static final String LIGUES_END_ERROR = "SEV-LIGUES-END-ERROR";

    /* CANCELAR LIGA */
    public static final String LIGUES_CANCEL = "CLI-LIGUES-CANCEL";
    public static final String LIGUES_CANCEL_OK = "SEV-LIGUES-CANCEL-OK";
    public static final String LIGUES_CANCEL_ERROR = "SEV-LIGUES-CANCEL-ERROR";

    /* LIGAS DE UN USUARIO */
    public static final String LIGUES_USER = "CLI-LIGUES-USER";
    public static final String LIGUES_USER_OK = "SEV-LIGUES-USER-OK";
    public static final String LIGUES_USER_ERROR = "SEV-LIGUES-USER-ERROR";

    /* LIGAS ESTADO? */
    public static final String LIGUES_STATE_USER = "CLI-LIGUES-STATE-USER";
    public static final String LIGUES_STATE_USER_OK = "SEV-LIGUES-STATE-USER-OK";
    public static final String LIGUES_STATE_USER_ERROR = "SEV-LIGUES-STATE-USER-ERROR";

    /* INSCRIBIR EQUIPO A LIGA */
    public static final String LIGUES_INSCRIBE_TEAM_USER = "CLI-LIGUES-INSCRIBE-TEAM-USER";
    public static final String LIGUES_INSCRIBE_TEAM_USER_OK = "SEV-LIGUES-INSCRIBE-TEAM-USER-OK";
    public static final String LIGUES_INSCRIBE_TEAM_USER_ERROR = "SEV-LIGUES-INSCRIBE-TEAM-USER-ERROR";

    /* EQUIPOS DISPONIBLES PARA UNA LIGA */
    public static final String LIGUES_TEAMS_INSCRIBE_USER = "CLI-LIGUES-TEAMS-INSCRIBE-USER";
    public static final String LIGUES_TEAMS_INSCRIBE_USER_OK = "SEV-LIGUES-TEAMS-INSCRIBE-USER-OK";
    public static final String LIGUES_TEAMS_INSCRIBE_USER_ERROR = "SEV-LIGUES-TEAMS-INSCRIBE-USER-ERROR";

    /* LIGAS DISPONIBLES */
    public static final String LIGUES_AVAILABLE = "CLI-LIGUES-AVAILABLE";
    public static final String LIGUES_AVAILABLE_OK = "SEV-LIGUES-AVAILABLE-OK";
    public static final String LIGUES_AVAILABLE_ERROR = "SEV-LIGUES-AVAILABLE-ERROR";

    /* PRIVACIDADES DE LOS EQUIPOS */
    public static final String TEAMS_PRIVACITY = "CLI-TEAMS-PRIVACITY";
    public static final String TEAMS_PRIVACITY_OK = "SEV-TEAMS-PRIVACITY-OK";
    public static final String TEAMS_PRIVACITY_ERROR = "SEV-TEAMS-PRIVACITY-ERROR";

    /* INVITACIONES DEL USUARIO */
    public static final String USER_INVITES = "CLI-USER-INVITES";
    public static final String USER_INVITES_OK = "SEV-USER-INVITES-OK";
    public static final String USER_INVITES_ERROR = "SEV-USER-INVITES-ERROR";

    /* RESPUESTA A INVITACION EL USUARIO */
    public static final String USER_ANSWER_INVITE = "CLI-USER-ANSWER-INVITE";
    public static final String USER_ANSWER_INVITE_OK = "SEV-USER-ANSWER-INVITE-OK";
    public static final String USER_ANSWER_INVITE_ERROR = "SEV-USER-ANSWER-INVITE-ERROR";

    /* BUSCAR EQUIPOS */
    public static final String SEARCH_TEAMS = "CLI-SEARCH-TEAMS";
    public static final String SEARCH_TEAMS_OK = "SEV-SEARCH-TEAMS-OK";
    public static final String SEARCH_TEAMS_ERROR = "SEV-SEARCH-TEAMS-ERROR";

    /* UNIRSE A EQUIPO */
    public static final String JOIN_TEAM = "CLI-JOIN-TEAM";
    public static final String JOIN_TEAM_OK = "SEV-JOIN-TEAM-OK";
    public static final String JOIN_TEAM_ERROR = "SEV-JOIN-TEAM-ERROR";

    /* SOLICITUD UNION A EQUIPO */
    public static final String SOLICTE_JOIN_TEAM = "CLI-SOLICTE-JOIN-TEAM";
    public static final String SOLICTE_JOIN_TEAM_OK = "SEV-SOLICTE-JOIN-TEAM-OK";
    public static final String SOLICTE_JOIN_TEAM_ERROR = "SEV-SOLICTE-JOIN-TEAM-ERROR";

    /* SOLICITUDES AL EQUIPO */
    public static final String TEAM_INVITES = "CLI-TEAM-INVITES";
    public static final String TEAM_INVITES_OK = "SEV-TEAM-INVITES-OK";
    public static final String TEAM_INVITES_ERROR = "SEV-TEAM-INVITES-ERROR";

    /* RESPUESTA A INSCRIPCIÓN EN EQUIPO */
    public static final String TEAM_ANSWER_INVITE = "CLI-TEAM-ANSWER-INVITE";
    public static final String TEAM_ANSWER_INVITE_OK = "SEV-TEAM-ANSWER-INVITE-OK";
    public static final String TEAM_ANSWER_INVITE_ERROR = "SEV-TEAM-ANSWER-INVITE-ERROR";

    /* COMPROBAR SI HA SOLICITADO UNION */
    public static final String USER_IS_INVITE = "CLI-USER-IS-INVITE";
    public static final String USER_IS_INVITE_OK = "SEV-USER-IS-INVITE-OK";
    public static final String USER_IS_INVITE_ERROR = "SEV-USER-IS-INVITE-ERROR";

    /* BORRAR EQUIPO */
    public static final String TEAM_DELETE = "CLI-TEAM-DELETE";
    public static final String TEAM_DELETE_OK = "SEV-TEAM-DELETE-OK";
    public static final String TEAM_DELETE_ERROR = "SEV-TEAM-DELETE-ERROR";

    /* ABANDONAR EQUIPO */
    public static final String TEAM_LEAVE = "CLI-TEAM-LEAVE";
    public static final String TEAM_LEAVE_OK = "SEV-TEAM-LEAVE-OK";
    public static final String TEAM_LEAVE_ERROR = "SEV-TEAM-LEAVE-ERROR";

    /* MODIFICAR EQUIPO */
    public static final String TEAM_MODIFY = "CLI-MODIFYTEAM";
    public static final String TEAM_MODIFY_OK = "SEV-MODIFYTEAM-OK";
    public static final String TEAM_MODIFY_ERROR = "SEV-MODIFYTEAM-ERROR";

    /* INSERTAR PAGO */
    public static final String INSERT_PAY = "CLI-INSERT-PAY";
    public static final String INSERT_PAY_OK = "SEV-INSERT-PAY-OK";
    public static final String INSERT_PAY_ERROR = "SEV-INSERT-PAY-ERROR";

    /* INSCRIBIR USUARIO A PARTIDO DE PAGO */
    public static final String GAMES_INSCRIBE_USER_PAY = "CLI-GAMES_INSCRIBE_PAY_USER";
    public static final String GAMES_INSCRIBE_USER_PAY_OK = "SEV-GAMES_INSCRIBE_PAY_USER-OK";
    public static final String GAMES_INSCRIBE_USER_PAY_ERROR = "SEV-GAMES_INSCRIBE_PAY_USER-ERROR";

    /* INSCRIBIR EQUIPO A LIGA DE PAGO */
    public static final String TOURNAMENTS_INSCRIBE_USER_PAY = "CLI-TOURNAMENTS_INSCRIBE_PAY_USER";
    public static final String TOURNAMENTS_INSCRIBE_USER_PAY_OK = "SEV-TOURNAMENTS_INSCRIBE_PAY_USER-OK";
    public static final String TOURNAMENTS_INSCRIBE_USER_PAY_ERROR = "SEV-TOURNAMENTS_INSCRIBE_PAY_USER-ERROR";

    /* INSCRIBIR EQUIPO A LIGA DE PAGO */
    public static final String LIGUES_INSCRIBE_USER_PAY = "CLI-LIGUES_INSCRIBE_PAY_USER";
    public static final String LIGUES_INSCRIBE_USER_PAY_OK = "SEV-LIGUES_INSCRIBE_PAY_USER-OK";
    public static final String LIGUES_INSCRIBE_USER_PAY_ERROR = "SEV-LIGUES_INSCRIBE_PAY_USER-ERROR";

    /* OBTENER CORREO */
    public static final String USER_EMAIL = "CLI-USER-MAIL";
    public static final String USER_EMAIL_OK = "SEV-USER-MAIL-OK";
    public static final String USER_EMAIL_ERROR = "SEV-USER-MAIL-ERROR";

    /* MODIFICAR PERFIL */
    public static final String CHANGE_DATA = "CLI-CHANGE-DATA";
    public static final String CHANGE_DATA_OK = "SEV-CHANGE-DATA-OK";
    public static final String CHANGE_DATA_ERROR = "SEV-CHANGE-DATA-ERROR";

    /* MOSTRAR DEPORTE ADMIND */
    public static final String GET_SPORT_ADMIN = "CLI-GET-SPORT";
    public static final String GET_SPORT_ADMIN_OK = "SEV-GET-SPORT-OK";
    public static final String GET_SPORT_ADMIN_ERROR = "SEV-GET-SPORT-ERROR";

    /* AÑADIR DEPORTE ADMIND */
    public static final String ADD_SPORT_ADMIN = "CLI-ADD-SPORT";
    public static final String ADD_SPORT_ADMIN_OK = "SEV-ADD-SPORT-OK";
    public static final String ADD_SPORT_ADMIN_ERROR = "SEV-ADD-SPORT-ERROR";

    /* MOSTRAR EQUIPOS ADMIN */
    public static final String SHOW_TEAM_ADMIN = "CLI-SHOW-TEAM";
    public static final String SHOW_TEAM_ADMIN_OK = "SEV-SHOW-TEAM-OK";
    public static final String SHOW_TEAM_ADMIN_ERROR = "SEV-SHOW-TEAM-ERROR";

    /* MOSTRAR USUARIO ADMIN */
    public static final String SHOW_USER_ADMIN = "CLI-SHOW-USER";
    public static final String SHOW_USER_ADMIN_OK = "SEV-SHOW-USER-OK";
    public static final String SHOW_USER_ADMIN_ERROR = "SEV-SHOW-USER-ERROR";

    /* DAR USUARIO ADMIN */
    public static final String ADD_USERADMIN_ADMIN = "CLI-ADD-USERADMIN";
    public static final String ADD_USERADMIN_ADMIN_OK = "SEV-ADD-USERADMIN-OK";
    public static final String ADD_USERADMIN_ADMIN_ERROR = "SEV-ADD-USERADMIN-ERROR";

    /* VER PARTIDOS ADMIN */
    public static final String SHOW_GAME_ADMIN = "CLI-SHOW-GAME";
    public static final String SHOW_GAME_ADMIN_OK = "SEV-SHOW-GAME-OK";
    public static final String SHOW_GAME_ADMIN_ERROR = "SEV-SHOW-GAME-ERROR";

    /* ELIMINTAR PARTIDOS ADMIN */
    public static final String DELETE_GAME_ADMIN = "CLI-DELETE-GAME";
    public static final String DELETE_GAME_ADMIN_OK = "SEV-DELETE-GAME-OK";
    public static final String DELETE_GAME_ADMIN_ERROR = "SEV-DELETE-GAME-ERROR";

    /*VER TORNEOS ADMIN*/
    public static final String SHOW_TOURNAMENT_ADMIN = "CLI-SHOW-TOURNAMENT";
    public static final String SHOW_TOURNAMENT_ADMIN_OK = "SEV-SHOW-TOURNAMENT-OK";
    public static final String SHOW_TOURNAMENT_ADMIN_ERROR = "SEV-SHOW-TOURNAMENT-ERROR";

    /*ELIMINTAR TORNEOS ADMIN*/
    public static final String DELETE_TOURNAMENT_ADMIN = "CLI-DELETE-TOURNAMENT";
    public static final String DELETE_TOURNAMENT_ADMIN_OK = "SEV-DELETE-TOURNAMENT-OK";
    public static final String DELETE_TOURNAMENT_ADMIN_ERROR = "SEV-DELETE-TOURNAMENT-ERROR";

    /* VER LIGAS ADMIN */
    public static final String SHOW_LIGUE_ADMIN = "CLI-SHOW-LIGUE";
    public static final String SHOW_LIGUE_ADMIN_OK = "SEV-SHOW-LIGUE-OK";
    public static final String SHOW_LIGUE_ADMIN_ERROR = "SEV-SHOW-LIGUE-ERROR";

    /* ELIMINTAR LIGAS ADMIN */
    public static final String DELETE_LIGUE_ADMIN = "CLI-DELETE-LIGUE";
    public static final String DELETE_LIGUE_ADMIN_OK = "SEV-DELETE-LIGUE-OK";
    public static final String DELETE_LIGUE_ADMIN_ERROR = "SEV-DELETE-LIGUE-ERROR";

    /* SOLICITAR DEPORTE */
    public static final String REQUEST_SPORT = "CLI-REQUEST-SPORT";
    public static final String REQUEST_SPORT_OK = "SEV-REQUEST-SPORT-OK";
    public static final String REQUEST_SPORT_ERROR = "SEV-REQUEST-SPORT-ERROR";

    /* ERROR */
    public static final String ERROR = "ERROR";

}