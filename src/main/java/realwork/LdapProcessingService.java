package realwork;

import idm.ldap.entity.AdUser;
import idm.ldap.entity.IsimUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 *  Пример работы по LDAP протоколу
 */
@Service
public class LdapProcessingService {
    protected LdapTemplate ldapTemplate;
    protected LdapTemplate ldapTemplate2;

    @Autowired
    public LdapProcessingService(LdapTemplate ldapTemplate, @Qualifier("ldapTemplate2") LdapTemplate ldapTemplate2) {
        this.ldapTemplate = ldapTemplate;
        this.ldapTemplate2 = ldapTemplate2;
    }

    public AdUser findAdUserByCard(String card) {
        var query = query()
                .base("ou=Sberbank,dc=delta,dc=sbrf,dc=ru")
                .where("objectclass").is("top")
                .and("objectclass").is("person")
                .and("objectclass").is("organizationalPerson")
                .and("objectclass").is("user")
                .and("cn").is(card);

        try {
            return ldapTemplate.findOne(query, AdUser.class);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }


    public IsimUser findIsimUserByCard(String card) {
        var query = query()
                .base("ou=people,erglobalid=00000000000000000000,ou=sbrf,DC=ITIM")
                .where("objectclass").is("top")
                .and("objectclass").is("sberPersons")
                .and("objectclass").is("erPersonItem")
                .and("objectclass").is("erManagedItem")
                .and("sberpdi").is(card);

        return ldapTemplate2.findOne(query, IsimUser.class);
    }
}