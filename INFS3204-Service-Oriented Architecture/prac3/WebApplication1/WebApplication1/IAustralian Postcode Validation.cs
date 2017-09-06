using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WebApplication1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IAustralian_Postcode_Validation" in both code and config file together.
    [ServiceContract]
    public interface IAustralian_Postcode_Validation
    {
        [OperationContract]
        void DoWork();

        [OperationContract]
        Boolean PostcodeValidation(int postcode, string state);
    }
}
