package modelo;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class Usuario {
    private final int usuarioID;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String DNI;


    private static final Pattern validacionNombreApellido = Pattern.compile("^[\\p{L}]{4,}$");

    private static final Pattern validacionEmail = Pattern.compile("^(?=.{1,254}$)(?=.{1,64}@)(?!.*\\.\\.)" + "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+" + "(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+" + "[A-Za-z]{2,}$");

    private static final Pattern validacionPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s])[\\S]{8,64}$");

    private static final Pattern validacionDNI = Pattern.compile("^\\d{8}$");


    public Usuario(int usuarioID, String nombre, String apellido, String email, String password, String DNI) {
        if(usuarioID <= 0){
            throw new IllegalArgumentException("El ID debe ser un numero > a 0");
        }

        this.usuarioID = usuarioID;

        setNombre(nombre);
        setApellido(apellido);
        setEmail(email);
        setPassword(password);
        setDNI(DNI);
    }

    private static void requiereNoNuloYNoVacio(String aValidar, String campo){
        if(aValidar == null){
            throw new IllegalArgumentException(campo + " no puede ser nulo");
        }

        if(aValidar.isBlank()){
            throw new IllegalArgumentException(campo + " no puede estar vacio");
        }
    }

    private static void requierePasarElRegex(String aValidar, Pattern validacion, String campo, String detalle){
        if(!validacion.matcher(aValidar).matches()){
            throw new IllegalArgumentException(campo + " invalido: " + detalle);
        }
    }

    private void setNombre(String nombre) {
        requiereNoNuloYNoVacio(nombre, "Nombre");
        requierePasarElRegex(nombre, validacionNombreApellido, "Nombre", "No cumple los requerimientos pedidos");

        this.nombre = nombre;
    }

    private void setApellido(String apellido) {
        requiereNoNuloYNoVacio(apellido, "Apellido");
        requierePasarElRegex(apellido, validacionNombreApellido, "Apellido", "No cumple los requerimientos pedidos");

        this.apellido = apellido;
    }

    private void setEmail(String email) {
        requiereNoNuloYNoVacio(email, "Email");
        String emailNecesario = email.trim().toLowerCase(Locale.ROOT);
        requierePasarElRegex(emailNecesario, validacionEmail, "Email", "No cumple los requerimientos pedidos");

        this.email = emailNecesario;
    }

    private void setPassword(String password) {
        requiereNoNuloYNoVacio(password, "Password");
        requierePasarElRegex(password, validacionPassword, "Password", "8–64, mayúscula, minúscula, dígito y símbolo, sin espacios");

        this.password = password;
    }

    private void setDNI(String DNI) {
        requiereNoNuloYNoVacio(DNI, "DNI");
        requierePasarElRegex(DNI, validacionDNI, "DNI", "debe tener exactamente 8 dígitos");

        this.DNI = DNI;
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDNI() {
        return DNI;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Usuario other)) return false;
        return ((usuarioID == other.usuarioID) && (email == other.email) && (DNI == other.DNI));
    }

    @Override
    public int hashCode(){
        return Objects.hash(usuarioID, email, DNI);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuarioID=" + usuarioID +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", DNI='" + DNI + '\'' +
                '}';
    }
}
