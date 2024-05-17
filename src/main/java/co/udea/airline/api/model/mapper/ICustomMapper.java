package co.udea.airline.api.model.mapper;

public interface ICustomMapper<S,T> {

    T convertToDto(S entity);
    S convertToEntity(T entityDTO);
}
