package com.unicartagena.apppaises.api;

import com.unicartagena.apppaises.model.Ciudad;
import com.unicartagena.apppaises.model.LoginResponse;
import com.unicartagena.apppaises.model.Pais;
import com.unicartagena.apppaises.model.Usuario;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Autenticación
    @POST("api/v1/auth/registro")
    Call<Usuario> registrar(@Body Usuario usuario);

    @POST("api/v1/auth/login")
    Call<LoginResponse> login(@Body Map<String, String> credenciales);

    // Países
    @GET("api/v1/paises")
    Call<List<Pais>> listarPaises(@Header("Authorization") String token);

    @POST("api/v1/paises")
    Call<Pais> crearPais(@Header("Authorization") String token, @Body Pais pais);

    @PUT("api/v1/paises/{id}")
    Call<Pais> actualizarPais(@Header("Authorization") String token, @Path("id") Long id, @Body Pais pais);

    @DELETE("api/v1/paises/{id}")
    Call<Void> eliminarPais(@Header("Authorization") String token, @Path("id") Long id);

    // Ciudades
    @GET("api/v1/ciudades")
    Call<List<Ciudad>> listarCiudades(@Header("Authorization") String token);

    @POST("api/v1/ciudades")
    Call<Ciudad> crearCiudad(@Header("Authorization") String token, @Body Ciudad ciudad);

    @PUT("api/v1/ciudades/{id}")
    Call<Ciudad> actualizarCiudad(@Header("Authorization") String token, @Path("id") Long id, @Body Ciudad ciudad);

    @DELETE("api/v1/ciudades/{id}")
    Call<Void> eliminarCiudad(@Header("Authorization") String token, @Path("id") Long id);
}