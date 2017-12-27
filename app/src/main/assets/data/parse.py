'''
This python script scrapes only the data I need regarding storage dates.
Credits to FSIS for their FoodKeeper Dataset: https://catalog.data.gov/dataset/fsis-foodkeeper-data
'''

import json

dict = {}
with open('foodkeeper.json', 'r') as infile, open('foodstorage.json', 'w') as outfile:
    data = json.load(infile)['sheets'][2]['data']
    for item in data:
        name = item[2]['Name']
        # fridge
        try:
            fridge = item[21]['DOP_Refrigerate_Max']
            if (fridge):
                fridge_metric = item[22]['DOP_Refrigerate_Metric']
                if (fridge_metric):
                    subdict = {
                        'fridge': fridge,
                        'fridge_metric': fridge_metric
                    }
                    dict[name] = subdict
        except(IndexError, KeyError):
            pass
        # freezer
        try:
            fridge = item[35]['DOP_Freeze_Max']
            if (fridge):
                fridge_metric = item[36]['DOP_Freeze_Metric']
                if (fridge_metric):
                    subdict = {
                        'freeze': fridge,
                        'freeze_metric': fridge_metric
                    }
                    myDict = dict[name]
                    if (myDict):
                        myDict.update(subdict)
                    else:
                        dict[name] = subdict
        except(IndexError, KeyError):
            pass
    json.dump(dict, outfile, indent=4)
        
