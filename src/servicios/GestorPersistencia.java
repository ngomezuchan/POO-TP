package servicios;

import interfaces.IPersistencia;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GestorPersistencia<T extends Serializable> implements IPersistencia<T> {
    
    private String nombreArchivo;
    private static final String DIRECTORIO_DATOS = "datos/";
    
    public GestorPersistencia(String nombreArchivo) {
        this.nombreArchivo = DIRECTORIO_DATOS + nombreArchivo;
        crearDirectorioDatos();
    }
    
    /**
     crea el directorio de datos si no existe
     */
    private void crearDirectorioDatos() {
        File directorio = new File(DIRECTORIO_DATOS);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
    
    /**
     Guarda un objeto en el archivo
     */
    @Override
    public void guardar(T objeto) throws Exception {
        List<T> lista = cargarTodos();
        lista.add(objeto);
        guardarLista(lista);
    }


    
    /**
     # carga todos los objetos del archivo
     */
    @Override
    public List<T> cargarTodos() throws Exception {
        File archivo = new File(nombreArchivo);
        
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return (List<T>) obj;
            } else {
                throw new Exception("El archivo no contiene una lista válida");
            }
        } catch (EOFException e) {
            // Archivo vacío
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error al cargar datos: " + e.getMessage(), e);
        }
    }





    /**
      guarda una lista losobjetos
     */
    public void guardarLista(List<T> lista) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(lista);
            oos.flush();
        } catch (IOException e) {
            throw new Exception("Error al guardar datos: " + e.getMessage(), e);
        }
    }

    public boolean existeArchivo() {
        File archivo = new File(nombreArchivo);
        return archivo.exists() && archivo.isFile();
    }

    public boolean eliminarArchivo() {
        File archivo = new File(nombreArchivo);
        if (archivo.exists()) {
            return archivo.delete();
        }
        return false;
    }

    
    //borre lo de la copia de seguridad, inecesario y cada tanto rompia el archivo
    
    
}
