#include <map>
#include <stack>
#include <vector>
#include <queue>
#include <sstream>
#include <string>
#include <cstdio>
#include <cstring>
#include <cmath>
#include <iostream>
#include <algorithm>
#include <set>
#define eps 1e-9
#define ALL(x) x.begin(), x.end()
#define INS(x) inderter(x, x.begin())
#define FOR(i,j,k) for(int i=j; j<=k;i++)
#define MAXN 1005
#define MAXM 40005
#define INF 0x3fffffff
using namespace std;
typedef long long LL;
LL i,j,k,n,m,x,y,T,big,cas,num;
bool flag;
LL cur, ans;
bool prim[2000005];
LL ver[2000005];

void GetPrim(LL size)
{
    LL m = sqrt(size+0.5);
    memset(prim, 0, sizeof(prim));
    num=0;
    for(LL i = 2; i <= size; i++){
        if(!prim[i])
        {
            ver[++num]=i;
            if(i<=m) for (LL j=i*i; j<=size;j+=i) prim[j] = 1;
        }
    }
}
class LCM
{
    public:
        int getMin(int N)
        {
            LL n = N;
            GetPrim(n);
            LL ans = n+1;
            for( i=num; i>=1; i-- )
            {
                LL u = ver[i];
                for(j=1; j*u<=n; j*=u);
                ans = max(ans, (n/j+1)*j);
            }
            return ans;
        }
};
int main(){
    int n,ans = 0;
    LCM lcm;
    cin >> n;
    cout << lcm.getMin(n) << endl;
    return 0;
}