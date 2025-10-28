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

    
    /**
     # crea la copia de seguridad del archivo
     */
    public boolean crearBackup() {
        File archivoOriginal = new File(nombreArchivo);
        if (!archivoOriginal.exists()) {
            return false;
        }
        
        String nombreBackup = nombreArchivo + ".backup";
        File archivoBackup = new File(nombreBackup);
        
        try (FileInputStream fis = new FileInputStream(archivoOriginal);
             FileOutputStream fos = new FileOutputStream(archivoBackup)) {
            
            byte[] buffer = new byte[1024];
            int longitud;
            
            while ((longitud = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, longitud);
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear backup: " + e.getMessage());
            return false;
        }
    }
    
    /**
     # vuelve desde la copia de seguridad
     */
    public boolean restaurarBackup() {
        String nombreBackup = nombreArchivo + ".backup";
        File archivoBackup = new File(nombreBackup);
        
        if (!archivoBackup.exists()) {
            return false;
        }
        
        File archivoOriginal = new File(nombreArchivo);
        
        try (FileInputStream fis = new FileInputStream(archivoBackup);
             FileOutputStream fos = new FileOutputStream(archivoOriginal)) {
            
            byte[] buffer = new byte[1024];
            int longitud;
            
            while ((longitud = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, longitud);
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al restaurar backup: " + e.getMessage());
            return false;
        }
    }
}
