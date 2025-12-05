package view;

/**
 * NavigationListener - Interface untuk handle navigasi antar view
 * Memungkinkan view berkomunikasi dengan Main untuk switch scene
 */
public interface NavigationListener {
    
    /**
     * Navigate ke view lain
     * @param viewName Nama view yang dituju
     * @param params Parameter yang diperlukan (bisa null)
     */
    void navigateTo(String viewName, String... params);
    
    /**
     * Kembali ke view sebelumnya
     */
    void goBack();
}
