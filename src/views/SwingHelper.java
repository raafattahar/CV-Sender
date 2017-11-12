package views;

import adapter.PropertiesHelper;
import model.PropertiesModelCache;

public class SwingHelper
{
    public static void deleteLogin()
    {
        if ( !PropertiesHelper.propertiesFileExists() )
            return;
        ProgressDialog progressDialog = new ProgressDialog("Deleting")
        {
            @Override
            public void run()
            {
                try
                {
                    setProgressValue(0.4);
                    PropertiesHelper.deleteLoginProperties();
                    setProgressValue(0.8);
                }
                finally
                {
                    closeProgressBar();
                }
            }
        };
        progressDialog.startJob();
    }

    public static void save(PropertiesModelCache modelCache)
    {
        ProgressDialog progressDialog = new ProgressDialog("Saving")
        {
            @Override
            public void run()
            {
                try
                {
                    setProgressValue(0.4);
                    PropertiesHelper.saveProperties(modelCache);
                    setProgressValue(0.8);
                }
                finally
                {
                    closeProgressBar();
                }
            }
        };
        progressDialog.startJob();
    }
}
