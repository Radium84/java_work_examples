package realwork;

open class TestDataUsersResponse {
        fun createAllFieldUser(): DslPart {
        return newJsonBody { o ->
        o.integerType("totalResults", 2)
        o.integerType("pageNumber", 1)
        o.integerType("pageSize", 10)
        o.array("resources") { a ->
        a.`object` { o2 ->
        o2.stringMatcher("id", "^[\\S]{1,40}$", "9999")
        o2.stringMatcher("loginName", "^[\\S]{1,64}$", "user1")
        o2.stringMatcher("familyName", "^[\\S]{1,128}$", "Ivanov")
        o2.stringType("givenName", "Ivan")
        o2.stringType("middleName", "Ivanovich")
        o2.stringType("position", "Lead Engineer")
        o2.stringType("positionId", "12345")
        o2.stringType("positionLong", "Lead Engineer")
        o2.booleanType("active", true)
        o2.`object`("emails") { o3 ->
        o3.stringType("internal", "user1@company.com")
        o3.stringType("external", "user1@gmail.com")
        }
        o2.array("roles") { a2 ->
        a2.stringType("role1")
        a2.stringType("role2")
        a2.stringType("role3")
        }
        o2.stringMatcher("employeeNumber", "^[\\S]{1,10}$", "1001")
        o2.stringType("hcmCode", "HCM001")
        o2.stringType("beCode", "38")
        o2.stringType("organization", "Organization 1")
        o2.`object`("orgUnit") { o4 ->
        o4.stringMatcher("id", "[a-z0-9]+", "9999")
        o4.stringType("name", "Org unit 1")
        o4.stringType("idPath", "b02a52b4")
        o4.stringType("path", "Org unit 1")
        o4.stringType("tbCode", "63")
        o4.stringType("branchCode", "1234")
        o4.stringType("subbranchCode", "12345")
        }
        o2.`object`("functionalBlock") { o5 ->
        o5.stringMatcher("id", "[a-z0-9]+", "38")
        o5.stringType("name", "Functional Block 1")
        }
        o2.array("insiderTypes") { a3 ->
        a3.stringType("type1")
        a3.stringType("type2")
        }
        o2.array("dirAItems") { a4 ->
        a4.stringType("aitem1")
        a4.stringType("aitem2")
        }
        o2.array("dirBItems") { a5 ->
        a5.stringType("bitem1")
        a5.stringType("bitem2")
        }
        o2.booleanType("isManager", false)
        }
        a.`object` { o3 ->
        o3.stringMatcher("id", "^[\\S]{1,40}$", "9998")
        o3.stringMatcher("loginName", "^[\\S]{1,64}$", "user2")
        o3.stringMatcher("familyName", "^[\\S]{1,128}$", "Ivanov1")
        o3.booleanType("active", false)
        o3.stringMatcher("employeeNumber", "^[\\S]{1,10}$", "10011")
        }
        }
        }.build()
        }
        }

        ExtendWith(PactConsumerTestExt::class)
        class GetUsersTest : TestDataUsersResponse(), TechMethods {


        @Pact(consumer = "generalprofile3connector--NGIG-D", provider = "sudir-common_scim-agent--SUDIR-D")
        fun getAllUsers(builder: PactDslWithProvider): RequestResponsePact {
        return builder
        .given("Positive Get all Users for GetUsers")
        .uponReceiving("Positive Get all Users for GetUsers")
        .method("GET")
        .path("/Users")
        .query("pageNumber=1&pageSize=10")
        .willRespondWith()
        .status(200)
        .headers(simpleHeaders())
        .body(
        createAllFieldUser()
        )
        .toPact()
