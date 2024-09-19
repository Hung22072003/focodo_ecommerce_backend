package focodo_ecommerce.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dpsqln4rh",
                "api_key", "898658922869354",
                "api_secret", "IJGSqZrIuZMCfE7q4OG5xW4dkn0",
                "secure", true));
    }
}
