package interfaces;

import java.util.List;


public interface IPersistencia<T> {
    void guardar(T objeto) throws Exception;
    List<T> cargarTodos() throws Exception;
}
//esto para guardar los objetos en la lista, le puse T.