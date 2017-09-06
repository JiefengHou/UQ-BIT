using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace Color_Mixer
{
    /// <summary>
    /// Summary description for Color_Mixer
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class Color_Mixer : System.Web.Services.WebService
    {

        [WebMethod]
        public string Mix(string color1, string color2)
        {
            switch (color1)
            {
                //red
                case "#FF0000":
                    {
                        //red and blue                                            
                        if (color2 == "#0000FF")
                            return "Purple";
                       
                        //red and green
                        if (color2 == "#008000")
                            return "Brown";
                     
                        //red and white
                        if (color2 == "#FFFFFF")
                            return "Pink";

                        //red and yellow
                        if (color2 == "#FFFF00")
                            return "Orange";
                       
                        else 
                            return "Not supported";                        
                    }

                //blue
                case "#0000FF":
                    {
                        //blue and yellow
                        if (color2 == "#FFFF00")
                            return "Green";

                        //blue and red
                        if (color2 == "#FF0000")
                            return "Purple";

                        else
                            return "Not supported"; 
                    }

                //yellow
                case "#FFFF00":
                    {
                        //yellow and red
                        if (color2 == "#FF0000")
                            return "Green";

                        //yellow and blue
                        if (color2 == "#0000FF")
                            return "Green";

                        //yellow and green
                        if (color2 == "#008000")
                            return "Light green";

                        else
                            return "Not supported";
                    }
                
                //green
                case "#008000":
                    {
                        //green and yellow
                        if (color2 == "#FFFF00")
                            return "Light green";

                        //green and red
                        if (color2 == "#FF0000")
                            return "Brown";

                        else
                            return "Not supported";
                    }

                //black
                case "#000000":
                    {
                        {
                            //black and white
                            if (color2 == "#FFFFFF")
                                return "Gray";

                            else
                                return "Not supported";
                        }
                    }

                //white
                case "#FFFFFF":
                    {
                        {
                            //white and black
                            if (color2 == "#000000")
                                return "Gray";

                            //white and red
                            if (color2 == "#FF0000")
                                return "Pink";

                            else
                                return "Not supported";
                        }
                    }

                default:
                    {
                        return "Not supported";
                    }
            }
        }
    }
}
