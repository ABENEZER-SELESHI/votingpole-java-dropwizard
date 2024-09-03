package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.example.auth.AppBasicAuthenticator;
import org.example.auth.AppAuthorizer;
import org.example.auth.User;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VotingPolesApplication extends Application<VotingPolesConfiguration> {

    public static void main(final String[] args) throws Exception {
        new VotingPolesApplication().run(args);
    }

    @Override
    public String getName() {
        return "VotingPoles";
    }

    @Override
    public void initialize(final Bootstrap<VotingPolesConfiguration> bootstrap) {
        // Initialization
    }

    @Override
    public void run(final VotingPolesConfiguration configuration, final Environment environment) {
        // MongoDB setup
        MongoClient mongoClient = MongoClients.create(configuration.getMongoUri());
        VotingPolesRepository repository = new VotingPolesRepository(mongoClient, "MartialPeak");
        VotingPolesService service = new VotingPolesService(repository);
        VotingPolesResource resource = new VotingPolesResource(service);

        // Registering the VotingPolesResource
        environment.jersey().register(resource);

        // Registering the StaticResource to serve static files (like favicon.ico)
        environment.jersey().register(new StaticResource());

        // Registering CORS Filter
        environment.servlets().addFilter("CORSFilter", new CORSFilter())
                .addMappingForUrlPatterns(null, false, "/*");

        // Register Basic Authenticator and Authorizer
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new AppBasicAuthenticator(configuration.getLogin(), configuration.getPassword()))
                        .setAuthorizer(new AppAuthorizer())
                        .setRealm("SUPER SECRET STUFF")
                        .buildAuthFilter()));

        // Register roles allowed feature and user binder for authentication
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    // Inner class for CORS Filter
    private class CORSFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            // Initialization logic if needed
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");

            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
            // Cleanup
        }
    }
}
