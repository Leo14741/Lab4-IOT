package com.example.lab4_iot.repo;

import com.example.lab4_iot.entity.Employee;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/obtenerDatosEmpleado/{employee_id}/{codigoTutor}")
    Call<Employee> obtenerDatosEmpleado(@Path("employee_id") String employee_id, @Path("codigoTutor") String codigoTutor);
    @PUT("/actualizarCita/{employee_id}")
    Call<Void> actualizarCita(@Path("employee_id") String employee_id);
}
