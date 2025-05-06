import javax.swing.*;
import java.awt.*;


public class PanelArbol extends JPanel{
    private cNodo raiz;
    private cNodo nodoEncontrado;

    public void setRaiz(cNodo raiz) {
        this.raiz = raiz;
    }

    public void setNodoEncontrado(cNodo nodoEncontrado) {
        this.nodoEncontrado = nodoEncontrado;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (raiz != null) {
            dibujarArbol(g, raiz, getWidth() / 2, 50, getWidth() / 4);
        }
    }

    private void dibujarArbol(Graphics g, cNodo nodo, int x, int y, int desplazamientoX) {
        if (nodo == null) return;

        g.setColor(nodoEncontrado != null &&
                nodo.getEmpleado().getNoEmpleado() == nodoEncontrado.getEmpleado().getNoEmpleado() ?
                Color.RED : Color.BLUE);

        g.fillOval(x - 20, y - 20, 40, 40);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(nodo.getEmpleado().getNoEmpleado()), x - 10, y + 5);

        g.setColor(Color.BLACK);
        if (nodo.getIzquierdo() != null) {
            int izquierdaX = x - desplazamientoX;
            int izquierdaY = y + 60;
            g.drawLine(x, y + 20, izquierdaX, izquierdaY - 20);
            dibujarArbol(g, nodo.getIzquierdo(), izquierdaX, izquierdaY, desplazamientoX / 2);
        }

        if (nodo.getDerecho() != null) {
            int derechaX = x + desplazamientoX;
            int derechaY = y + 60;
            g.drawLine(x, y + 20, derechaX, derechaY - 20);
            dibujarArbol(g, nodo.getDerecho(), derechaX, derechaY, desplazamientoX / 2);
        }
    }
}
