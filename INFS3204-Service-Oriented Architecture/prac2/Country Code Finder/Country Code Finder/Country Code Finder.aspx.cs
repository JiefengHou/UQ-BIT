using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;


namespace Country_Code_Finder
{
    public partial class Country_Code_Finder : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            CountryCodeFinderRef.Country_Code_Finder1 currentTime = new CountryCodeFinderRef.Country_Code_Finder1();
            Label1.Text = currentTime.dispalyTime();  
        }

    }
}