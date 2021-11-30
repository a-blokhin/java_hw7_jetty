package server;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("NotNullNullableValidation")
public final class SecurityHandlerBuilder {
    private static final String ROLE_MANAGER = "manager";
    private static final String ROLE_GUEST = "guest";

    private final ConstraintSecurityHandler security = new ConstraintSecurityHandler();

    public final ConstraintSecurityHandler build(LoginService loginService) {
        security.setLoginService(loginService);

        final List<ConstraintMapping> constraintMappings = new ArrayList<>();

        Stream.of("GET", "HEAD", "OPTIONS").forEach(method -> {
            constraintMappings.addAll(constraintMapping(
                    buildConstraint(ROLE_GUEST, ROLE_MANAGER),
                    Collections.singletonList("/getProducts"),
                    method));
        } );

        Stream.of("POST").forEach(method -> {
            constraintMappings.addAll(constraintMapping(
                    buildConstraint(ROLE_GUEST, ROLE_MANAGER),
                    Collections.singletonList("/postProduct"),
                    method));
        } );

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
