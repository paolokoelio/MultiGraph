package es.um.multigraph.conf;

/**
 * A map of primitives declared in the MulVAL' s rule file (rule_m1_2.P)
 * @see TODO place URL
 * @author Pavlo Burda
 */
public enum MulVALPrimitives {
    /**
     * Generic Primitive == AND
     */
    GENERIC("Generic Primitive"),
    /**
     * Generic Derived Primitive == OR
     */
    DERIVED_GENERIC("Generic Derived Primitive"),

    /**
     * AND (primitives)
     */
    AND("AND"),
    /**
     * attackerLocated(_location)
     */
    ATTACKER("attackerLocated"),
    /**
     * hacl(_src, _dst, _prot, _port)
     */
    HACL("hacl"),
    /**
     * vulExists(_host, _vulID, _CVSSAC, _program)
     */
    VULN("vulExists"),
    /**
     * vulProperty(_vulID, _range, _consequence)
     */
    VULN_PROP("vulProperty"),
    /**
     * networkServiceInfo(_host, _program, _protocol, _port, _user)
     */
    SERV_INFO("networkServiceInfo"),
    /**
     * progRunning(_program, _host)
     */
    PROG_RUN("progRunning"),
    /**
     * OR (aka derived primitives)
     */
    OR("OR"),
    /**
     * execCode(_host, _permission)
     */
    EXEC("execCode"),
    /**
     * netAccess(_host, _protocol, _port)
     */
    NET_ACCESS("netAccess"),
    /**
     * nfsExportInfo(_server, _path, _access, _client)
     */
    NFS_EXP("nfsExportInfo"),
    /**
     * inCompetent(_principal)
     */
    INCOMPETENT("inCompetent"),
    /**
     * accessFile(_machine,_access,_filepath)
     */
    ACCESSFILE("accessFile"),
    ;

    private MulVALPrimitives() {
        this.value = "Generic Primitives Enum";
    }
    
    private final String value;

    MulVALPrimitives(String value) {
        this.value = value;
    }

	public String getValue() {
		return value;
	}
    
}
