package si.uni.lj.fri.lg0775.api;

import com.kumuluz.ee.cors.annotations.CrossOrigin;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@CrossOrigin()
@ApplicationPath("v1")
public class FeatureFlagsApp extends Application {
}