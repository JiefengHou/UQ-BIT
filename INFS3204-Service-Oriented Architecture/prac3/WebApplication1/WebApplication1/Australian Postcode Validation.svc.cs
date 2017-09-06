using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WebApplication1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Australian_Postcode_Validation" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Australian Postcode Validation.svc or Australian Postcode Validation.svc.cs at the Solution Explorer and start debugging.
    public class Australian_Postcode_Validation : IAustralian_Postcode_Validation
    {
        public void DoWork()
        {
        }

        public Boolean PostcodeValidation(int postcode, string state)
        {
            switch (state)
            {
                case "NSW":
                    {
                        if (postcode >= 2000 && postcode <= 2599)
                        {
                            return true;
                        }

                        else if (postcode >= 2619 && postcode <= 2898)
                        {
                            return true;
                        }

                        else if (postcode >= 2921 && postcode <= 2999)
                        {
                            return true;
                        }

                        else return false;
                    }

                case "ACT":
                    {
                        if (postcode >= 2600 && postcode <= 2618)
                        {
                            return true;
                        }

                        else if (postcode >= 2900 && postcode <= 2920)
                        {
                            return true;
                        }

                        else return false;
                    }

                case "VIC":
                    {
                        if (postcode >= 3000 && postcode <= 3999)
                        {
                            return true;
                        }

                        else return false;
                    }

                case "QLD":
                    {
                        if (postcode >= 4000 && postcode <= 4999)
                        {
                            return true;
                        }

                        else return false;
                    }

                case "SA":
                    {
                        if (postcode >= 5000 && postcode <= 5799)
                        {
                            return true;
                        }

                        else return false;
                    }

                case "WA":
                    {
                        if (postcode >= 6000 && postcode <= 6797)
                        {
                            return true;
                        }

                        else return false;
                    }

                case "TAS":
                    {
                        if (postcode >= 7000 && postcode <= 7799)
                        {
                            return true;
                        }

                        else return false;
                    }

                case "NT":
                    {
                        if (postcode >= 0800 && postcode <= 0899)
                        {
                            return true;
                        }

                        else return false;
                    }

                default:
                    {
                        return false;
                    }
            }

        }

    }
}
