import com.sugaronrest.*;
import com.sugaronrest.modules.Contacts;

import java.util.ArrayList;
import java.util.List;

public class TestJersey {

	public static SugarRestResponse bulkReadContact(SugarRestClient client, int count) {
        SugarRestRequest request = new SugarRestRequest("Contacts", RequestType.BulkRead);
        request.getOptions().setSelectFields(getSelectedField());
        request.getOptions().getSelectFields().add(NameOf.Contacts.Id);

        request.getOptions().setMaxResult(count);

        return client.execute(request);
    }
	
	public static SugarRestResponse readContact(SugarRestClient client, String contactId) {
        SugarRestRequest request = new SugarRestRequest("Contacts", RequestType.ReadById);
        request.setParameter(contactId);
        request.getOptions().setSelectFields(getSelectedField());
        request.getOptions().getSelectFields().add(NameOf.Contacts.Id);

        return client.execute(request);
    }

    public static List<String> getSelectedField()
    {
        List<String> selectedFields = new ArrayList<String>();

        selectedFields.add(NameOf.Contacts.FirstName);
        selectedFields.add(NameOf.Contacts.LastName);
        selectedFields.add(NameOf.Contacts.Title);
        selectedFields.add(NameOf.Contacts.Description);
        selectedFields.add(NameOf.Contacts.PrimaryAddressPostalcode);

        return selectedFields;
    }

    public static void main(String[] args) {
        SugarRestClient client = new SugarRestClient();
        client.setUrl("https://crm.lionsharefinancialgroup.com/service/v4_1/rest.php");
        client.setUsername("rajeshmalani");
        client.setPassword("Change*12Flk21");

        SugarRestResponse res = readContact(client,"62fdf732-89c4-0c84-d64b-5f3645d5d926");
        System.out.println(res.getJData());
//        SugarRestResponse res = bulkReadContact(client,3);
//        System.out.println(res.getStatusCode());
//        List<Contacts> contacts= (List<Contacts>) res.getData();
//        for (Contacts obj:
//             contacts) {
//            System.out.println(obj.getId());
//        }
//        62fdf732-89c4-0c84-d64b-5f3645d5d926
    }

}
