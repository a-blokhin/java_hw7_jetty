package server;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("NotNullNullableValidation")
public final class SecurityHandlerBuilder {
    private static final String ROLE_MANAGER = "manager";
    private static final String ROLE_GUEST = "guest";

    private final ConstraintSecurityHandler security = new ConstraintSecurityHandler();

    public final ConstraintSecurityHandler build(LoginService loginService) {
        security.setLoginService(loginService);

        final List<ConstraintMapping> constraintMappings = new ArrayList<>();
        constraintMappings.addAll(constraintPostMapping(
                buildConstraint(ROLE_MANAGER),
                Collections.singletonList("/postProduct")
        ));

        constraintMappings.addAll(constraintGetMapping(
                buildConstraint(ROLE_GUEST, ROLE_MANAGER),
                Collections.singletonList("/getProducts")
        ));

        constraintMappings.addAll(constraintHeadMapping(
                buildConstraint(ROLE_GUEST, ROLE_MANAGER),
                Collections.singletonList("/getProducts")
        ));

        constraintMappings.addAll(constraintOptionsMapping(
                buildConstraint(ROLE_GUEST, ROLE_MANAGER),
                Collections.singletonList("/getProducts")
        ));

        security.setConstraintMappings(constraintMappings);
        security.setAuthenticator(new BasicAuthenticator());
        security.setDenyUncoveredHttpMethods(true);
        return security;
    }

    private static Constraint buildConstraint(String... userRoles) {
        final Constraint starterConstraint = new Constraint();
        starterConstraint.setName(Constraint.__BASIC_AUTH);
        starterConstraint.setRoles(userRoles);
        starterConstraint.setAuthenticate(true);
        return starterConstraint;
    }

    private static Collection<ConstraintMapping> constraintGetMapping(Constraint constraint,
                                                                      Collection<String> paths) {
        return constraintMapping(constraint, paths, "GET");
    }

    private static Collection<ConstraintMapping> constraintHeadMapping(Constraint constraint,
                                                                      Collection<String> paths) {
        return constraintMapping(constraint, paths, "HEAD");
    }

    private static Collection<ConstraintMapping> constraintOptionsMapping(Constraint constraint,
                                                                      Collection<String> paths) {
        return constraintMapping(constraint, paths, "OPTIONS");
    }

    private static Collection<ConstraintMapping> constraintPostMapping(Constraint constraint,
                                                                       Collection<String> paths) {
        return constraintMapping(constraint, paths, "POST");
    }

    private static Collection<ConstraintMapping> constraintMapping(Constraint constraint,
                                                                   Collection<String> paths,
                                                                   String method) {
        return paths.stream()
                .map(path -> {
                            final ConstraintMapping mapping = new ConstraintMapping();
                            mapping.setConstraint(constraint);
                            mapping.setPathSpec(path);
                            mapping.setMethod(method);
                            return mapping;
                        }
                ).collect(Collectors.toList());
    }
}
