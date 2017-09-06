using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace Color_Mixer
{
    /// <summary>
    /// Summary description for Color_to_Code_Convertor
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class Color_to_Code_Convertor : System.Web.Services.WebService
    {

        [WebMethod]
        public string Convert(string color)
        {
            switch (color)
            {
                case "White":
                    {
                        return "#FFFFFF";
                    }
                case "Red":
                    {
                        return "#FF0000";
                    }
                case "Blue":
                    {
                        return "#0000FF";
                    }
                case "Green":
                    {
                        return "#008000";
                    }
                case "Yellow":
                    {
                        return "#FFFF00";
                    }
                case "Black":
                    {
                        return "#000000";
                    }
                default:
                    {
                        return "Not found";
                    }
            }

        }
    }
}
