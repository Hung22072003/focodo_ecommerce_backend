package focodo_ecommerce.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class cloudinary {
    private static Cloudinary instance;

    private cloudinary(){}

    public static synchronized Cloudinary getInstance(){
        if(instance == null){
            instance = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dpsqln4rh",
                    "api_key", "898658922869354",
                    "api_secret", "IJGSqZrIuZMCfE7q4OG5xW4dkn0",
                    "secure", true));
        }
        return instance;
    }
}
